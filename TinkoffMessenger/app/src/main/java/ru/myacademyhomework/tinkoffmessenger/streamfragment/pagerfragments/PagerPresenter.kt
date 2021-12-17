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
import ru.myacademyhomework.tinkoffmessenger.di.ApiClient
import ru.myacademyhomework.tinkoffmessenger.di.pager.PagerScope
import ru.myacademyhomework.tinkoffmessenger.network.Topic
import javax.inject.Inject

@PagerScope
class PagerPresenter @Inject constructor(private val chatDao: ChatDao, private val apiClient: ApiClient) :
    BasePresenter<PagerView>() {

    private var databaseIsNotEmpty = false
    private var databaseIsRefresh = false

    fun openChatTopic(topic: Topic) {
        viewState.openChatTopic(topic)
    }

    fun openChatStream(stream: Stream){
        viewState.openChatStream(stream)
    }

    fun openNewStreamFragment(){
        viewState.openNewStreamFragment()
    }

    fun getSubscribedStreamsFromDb() {
        chatDao
            .getSubscribedStreams()
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

    fun getAllStreamsFromDb() {
        chatDao
            .getAllStreams()
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
                if (!databaseIsRefresh) getAllStreams()
            }, {
            })
            .addTo(compositeDisposable)
    }

    fun getStreams() {
        apiClient.chatApi.getStreams()
            .subscribeOn(Schedulers.io())
            .flatMap {
                Single.just(it.subscriptions)
            }
            .flatMapObservable {
                Observable.fromIterable(it)
            }
            .flatMap { subscription ->
                apiClient.chatApi.getTopics(subscription.streamID)
                    .map { topicResponse ->
                        chatDao.insertTopics(topicResponse.topics.map { topic ->
                            TopicDb(topic.name, subscription.name, subscription.streamID)
                        })
                        StreamDb(subscription.streamID, subscription.name, true)
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

    fun getAllStreams(){
        apiClient.chatApi.getAllStreams()
            .subscribeOn(Schedulers.io())
            .flatMap {
                Single.just(it.streams)
            }
            .flatMapObservable {
                Observable.fromIterable(it)
            }
            .flatMap { stream ->
                apiClient.chatApi.getTopics(stream.streamID)
                    .map { topicResponse ->
                        chatDao.insertTopics(topicResponse.topics.map { topic ->
                            TopicDb(topic.name, stream.name, stream.streamID)
                        })
                        StreamDb(stream.streamID, stream.name, false)
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