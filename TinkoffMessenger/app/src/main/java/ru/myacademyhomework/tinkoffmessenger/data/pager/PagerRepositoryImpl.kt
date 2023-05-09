package ru.myacademyhomework.tinkoffmessenger.data.pager

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.myacademyhomework.tinkoffmessenger.domain.pager.Stream
import ru.myacademyhomework.tinkoffmessenger.data.database.ChatDao
import ru.myacademyhomework.tinkoffmessenger.data.database.model.StreamDb
import ru.myacademyhomework.tinkoffmessenger.data.mapper.TopicMapper
import ru.myacademyhomework.tinkoffmessenger.di.ApiClient
import ru.myacademyhomework.tinkoffmessenger.domain.pager.PagerRepository
import javax.inject.Inject

class PagerRepositoryImpl @Inject constructor(
    private val apiClient: ApiClient,
    private val chatDao: ChatDao,
    private val topicMapper: TopicMapper
): PagerRepository {

    override fun getSubscribeStreamsFromDb(): Flowable<List<Stream>> {
        return chatDao
            .getSubscribedStreams()
            .map {
                it.map { streamDb ->
                    val listTopics = chatDao.getTopics(streamDb.nameChannel).map { topicDb ->
                        topicMapper.mapDbModelToEntity(topicDb)
                    }
                    Stream(streamDb.nameChannel, listTopics)
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getAllStreamsFromDb(): Flowable<List<Stream>> {
        return chatDao
            .getAllStreams()
            .map {
                it.map { streamDb ->
                    val listTopics = chatDao.getTopics(streamDb.nameChannel).map { topicDb ->
                        topicMapper.mapDbModelToEntity(topicDb)
                    }
                    Stream(streamDb.nameChannel, listTopics)
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getTopicsFromDb(streamName: String): Single<Stream> {
        return chatDao.getTopicsForStream(streamName)
            .map { listTopicsDb ->
                listTopicsDb.map { topicDb ->
                    topicMapper.mapDbModelToEntity(topicDb)
                }
            }.map {
                Stream(streamName, it)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getSubscribeStreams(): Single<MutableList<StreamDb>> {
        return apiClient.chatApi.getStreams()
            .subscribeOn(Schedulers.io())
            .flatMap {
                Single.just(it.subscriptions)
            }
            .flatMapObservable {
                Observable.fromIterable(it)
            }
            .flatMapSingle { subscription ->
                apiClient.chatApi.getTopics(subscription.streamID)
                    .map { topicResponse ->
                        chatDao.insertTopics(topicResponse.topics.map { topic ->
                            topicMapper.mapDtoToDbModel(topic,subscription)
                        })
                        StreamDb(subscription.streamID, subscription.name, true)
                    }
            }
            .toList()
            .doOnSuccess {
                chatDao.insertStream(it)
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getAllStreams(): Single<MutableList<StreamDb>> {
        return  apiClient.chatApi.getAllStreams()
            .subscribeOn(Schedulers.io())
            .flatMap {
                Single.just(it.streams)
            }
            .flatMapObservable {
                Observable.fromIterable(it)
            }
            .flatMapSingle { stream ->
                apiClient.chatApi.getTopics(stream.streamID)
                    .map { topicResponse ->
                        chatDao.insertTopics(topicResponse.topics.map { topic ->
                            topicMapper.mapDtoToDbModel(topic, stream)
                        })
                        StreamDb(stream.streamID, stream.name, false)
                    }
            }
            .toList()
            .doOnSuccess {
                chatDao.insertStream(it)
            }
            .observeOn(AndroidSchedulers.mainThread())
    }
}