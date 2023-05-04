package ru.myacademyhomework.tinkoffmessenger.presentation.streamfragment.pagerfragment

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import ru.myacademyhomework.tinkoffmessenger.common.BasePresenter
import ru.myacademyhomework.tinkoffmessenger.data.Stream
import ru.myacademyhomework.tinkoffmessenger.data.database.ChatDao
import ru.myacademyhomework.tinkoffmessenger.data.database.model.StreamDb
import ru.myacademyhomework.tinkoffmessenger.data.database.model.TopicDb
import ru.myacademyhomework.tinkoffmessenger.di.ApiClient
import ru.myacademyhomework.tinkoffmessenger.di.pager.PagerScope
import ru.myacademyhomework.tinkoffmessenger.data.network.model.Topic
import ru.myacademyhomework.tinkoffmessenger.domain.pager.GetAllStreamsFromDbUseCase
import ru.myacademyhomework.tinkoffmessenger.domain.pager.GetAllStreamsUseCase
import ru.myacademyhomework.tinkoffmessenger.domain.pager.GetSubscribeStreamsFromDbUseCase
import ru.myacademyhomework.tinkoffmessenger.domain.pager.GetSubscribeStreamsUseCase
import ru.myacademyhomework.tinkoffmessenger.domain.pager.GetTopicsFromDbUseCase
import javax.inject.Inject

@PagerScope
class PagerPresenter @Inject constructor(
    private val getSubscribeStreamsFromDbUseCase: GetSubscribeStreamsFromDbUseCase,
    private val getAllStreamsFromDbUseCase: GetAllStreamsFromDbUseCase,
    private val getTopicsFromDbUseCase: GetTopicsFromDbUseCase,
    private val getSubscribeStreamsUseCase: GetSubscribeStreamsUseCase,
    private val getAllStreamsUseCase: GetAllStreamsUseCase
) : BasePresenter<PagerView>() {

    private var databaseIsNotEmpty = false
    private var databaseIsRefresh = false

    fun openChatTopic(topic: Topic) {
        viewState.openChatTopic(topic)
    }

    fun openChatStream(stream: Stream) {
        viewState.openChatStream(stream)
    }

    fun openNewStreamFragment() {
        viewState.openNewStreamFragment()
    }

    fun getSubscribedStreamsFromDb() {
//        chatDao
//            .getSubscribedStreams()
//            .map {
//                it.map { streamDb ->
//                    val listTopics = chatDao.getTopics(streamDb.nameChannel).map { topicDb ->
//                        Topic(topicDb.streamId, topicDb.nameTopic, topicDb.nameStream)
//                    }
//                    Stream(streamDb.nameChannel, listTopics)
//                }
//            }
//            .observeOn(AndroidSchedulers.mainThread())
        getSubscribeStreamsFromDbUseCase.getSubscribeStreamsFromDb()
            .subscribe({
                if (it.isEmpty()) {
                    viewState.showRefresh()
                    getSubscribeStreams()
                } else {
                    databaseIsNotEmpty = true
                    viewState.setDataToRecycler(it)
                }
                if (!databaseIsRefresh) getSubscribeStreams()
            }, {
            })
            .addTo(compositeDisposable)
    }

    fun getAllStreamsFromDb() {
//        chatDao
//            .getAllStreams()
//            .map {
//                it.map { streamDb ->
//                    val listTopics = chatDao.getTopics(streamDb.nameChannel).map { topicDb ->
//                        Topic(topicDb.streamId, topicDb.nameTopic, topicDb.nameStream)
//                    }
//                    Stream(streamDb.nameChannel, listTopics)
//                }
//            }
//            .observeOn(AndroidSchedulers.mainThread())
        getAllStreamsFromDbUseCase.getAllStreamsFromDb()
            .subscribe({
                if (it.isEmpty()) {
                    viewState.showRefresh()
                    getSubscribeStreams()
                } else {
                    databaseIsNotEmpty = true
                    viewState.setDataToRecycler(it)
                }
                if (!databaseIsRefresh) getAllStreams()
            }, {
            })
            .addTo(compositeDisposable)
    }

    fun getTopicsFromDb(streamName: String) {
//        chatDao.getTopicsForStream(streamName)
//            .map { listTopicsDb ->
//                listTopicsDb.map { topicDb ->
//                    Topic(topicDb.streamId, topicDb.nameTopic, topicDb.nameStream)
//                }
//            }.map {
//                Stream(streamName, it)
//            }
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
        getTopicsFromDbUseCase.getTopicsFromDb(streamName)
            .subscribe({
                viewState.setDataToRecycler(listOf(it))
            }, {

            })
            .addTo(compositeDisposable)
    }

    fun getSubscribeStreams() {
//        apiClient.chatApi.getStreams()
//            .subscribeOn(Schedulers.io())
//            .flatMap {
//                Single.just(it.subscriptions)
//            }
//            .flatMapObservable {
//                Observable.fromIterable(it)
//            }
//            .flatMapSingle { subscription ->
//                apiClient.chatApi.getTopics(subscription.streamID)
//                    .map { topicResponse ->
//                        chatDao.insertTopics(topicResponse.topics.map { topic ->
//                            TopicDb(topic.name, subscription.name, subscription.streamID)
//                        })
//                        StreamDb(subscription.streamID, subscription.name, true)
//                    }
//            }
//            .toList()
//            .doOnSuccess {
//                chatDao.insertStream(it)
////                databaseIsRefresh = true
//            }
//            .observeOn(AndroidSchedulers.mainThread())
        getSubscribeStreamsUseCase.getSubscribeStreams()
            .doOnSubscribe {
                if (!databaseIsNotEmpty) {
                    viewState.showRefresh()
                }
            }
            .subscribe({
                databaseIsRefresh = true
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

    private fun getAllStreams() {
//        apiClient.chatApi.getAllStreams()
//            .subscribeOn(Schedulers.io())
//            .flatMap {
//                Single.just(it.streams)
//            }
//            .flatMapObservable {
//                Observable.fromIterable(it)
//            }
//            .flatMapSingle { stream ->
//                apiClient.chatApi.getTopics(stream.streamID)
//                    .map { topicResponse ->
//                        chatDao.insertTopics(topicResponse.topics.map { topic ->
//                            TopicDb(topic.name, stream.name, stream.streamID)
//                        })
//                        StreamDb(stream.streamID, stream.name, false)
//                    }
//            }
//            .toList()
//            .doOnSuccess {
//                chatDao.insertStream(it)
////                databaseIsRefresh = true
//            }
//            .observeOn(AndroidSchedulers.mainThread())
        getAllStreamsUseCase.getAllStreams()
            .doOnSubscribe {
                if (!databaseIsNotEmpty) {
                    viewState.showRefresh()
                }
            }
            .subscribe({
                databaseIsRefresh = true
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