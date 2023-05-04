package ru.myacademyhomework.tinkoffmessenger.domain.pager

import io.reactivex.Flowable
import io.reactivex.Single
import ru.myacademyhomework.tinkoffmessenger.data.Stream
import ru.myacademyhomework.tinkoffmessenger.data.database.model.StreamDb

interface PagerRepository {
    fun getSubscribeStreamsFromDb(): Flowable<List<Stream>>
    fun getAllStreamsFromDb(): Flowable<List<Stream>>
    fun getTopicsFromDb(streamName: String): Single<Stream>
    fun getSubscribeStreams(): Single<MutableList<StreamDb>>
    fun getAllStreams(): Single<MutableList<StreamDb>>
}