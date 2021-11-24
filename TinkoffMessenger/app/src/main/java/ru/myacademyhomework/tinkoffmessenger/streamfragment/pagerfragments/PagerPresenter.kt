package ru.myacademyhomework.tinkoffmessenger.streamfragment.pagerfragments

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import ru.myacademyhomework.tinkoffmessenger.common.BasePresenter
import ru.myacademyhomework.tinkoffmessenger.data.Stream
import ru.myacademyhomework.tinkoffmessenger.database.ChatDao
import ru.myacademyhomework.tinkoffmessenger.database.StreamDb
import ru.myacademyhomework.tinkoffmessenger.database.TopicDb
import ru.myacademyhomework.tinkoffmessenger.network.RetrofitModule
import ru.myacademyhomework.tinkoffmessenger.network.Topic

class PagerPresenter(private val chatDao: ChatDao) : BasePresenter<PagerView>() {

    private var databaseIsNotEmpty = false
    private var databaseIsRefresh = false

    fun openChatTopic(topic: Topic) {
        viewState.openChatTopic(topic)
    }

    fun getStreamsFromDb() {
        chatDao
            .getStreams()
            .map {
                it.map { streamDb ->
                    val listTopics = chatDao.getTopics(streamDb.nameChannel).map { topicDb ->
                        Topic(topicDb.streamId, topicDb.nameTopic, topicDb.nameStream)
                    }
                    Stream(streamDb.nameChannel, listTopics)
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isEmpty()) {
                    viewState.showRefresh()
                    getStreams()
                } else {
                    databaseIsNotEmpty = true
                    viewState.setDataToRecycler(it)
                }
                if (!databaseIsRefresh) getStreams()
            }, {
            })
            .addTo(compositeDisposable)
    }

    fun getStreams() {
        val chatApi = RetrofitModule.chatApi
        chatApi.getStreams()
            .subscribeOn(Schedulers.io())
            .flatMap {
                Single.just(it.subscriptions)
            }
            .flatMapObservable {
                Observable.fromIterable(it)
            }
            .flatMap { subscription ->
                chatApi.getTopics(subscription.streamID)
                    .map { topicResponse ->
                        chatDao.insertTopics(topicResponse.topics.map { topic ->
                            TopicDb(topic.name, subscription.name, subscription.streamID)
                        })
                        StreamDb(subscription.streamID, subscription.name)
                    }
            }
            .toList()
            .doOnSuccess {
                chatDao.insertStream(it)
                databaseIsRefresh = true
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                if (!databaseIsNotEmpty) {
                    viewState.showRefresh()
                }
            }
            .subscribe({
                viewState.hideRefresh()
            }, {
                if (databaseIsNotEmpty) {
                    viewState.showErrorUpdateData()
                } else {
                    viewState.showError()
                }
            })
            .addTo(compositeDisposable)
    }
}