package ru.myacademyhomework.tinkoffmessenger.data.stream

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import ru.myacademyhomework.tinkoffmessenger.data.database.ChatDao
import ru.myacademyhomework.tinkoffmessenger.data.database.model.StreamDb
import ru.myacademyhomework.tinkoffmessenger.data.mapper.StreamMapper
import ru.myacademyhomework.tinkoffmessenger.data.network.ChatApi
import ru.myacademyhomework.tinkoffmessenger.di.ApiClient
import ru.myacademyhomework.tinkoffmessenger.di.stream.StreamScope
import ru.myacademyhomework.tinkoffmessenger.domain.stream.StreamInfo
import ru.myacademyhomework.tinkoffmessenger.domain.stream.StreamRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@StreamScope
class StreamRepositoryImpl @Inject constructor(
    private val apiClient: ApiClient,
    private val chatDao: ChatDao,
    private val mapper: StreamMapper
): StreamRepository {
    private val subject = PublishSubject.create<String>()

    override fun search(str: String) {
        subject.onNext(str)
    }

    override fun initSearch(): Observable<StreamInfo> {
        return subject
            .filter { str -> str.isNotEmpty() }
            .distinctUntilChanged()
            .debounce(1000, TimeUnit.MILLISECONDS)
            .switchMap { str ->
                val stream = chatDao.getStream(str)
                if (stream != null) {
                    Observable.just(mapper.mapDbModelToEntity(stream))
                }
                else {
                    Observable.just(StreamInfo(0, "", false))
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}