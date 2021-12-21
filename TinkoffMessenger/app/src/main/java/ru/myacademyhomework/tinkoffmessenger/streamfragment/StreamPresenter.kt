package ru.myacademyhomework.tinkoffmessenger.streamfragment

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import ru.myacademyhomework.tinkoffmessenger.common.BasePresenter
import ru.myacademyhomework.tinkoffmessenger.database.ChatDao
import ru.myacademyhomework.tinkoffmessenger.database.StreamDb
import ru.myacademyhomework.tinkoffmessenger.di.stream.StreamScope
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@StreamScope
class StreamPresenter @Inject constructor(private val chatDao: ChatDao) :
    BasePresenter<StreamView>() {

    private var isSearch = false
    private val subject = PublishSubject.create<String>()

    fun search(str: String) {
        if (str.isEmpty() && isSearch) {
            viewState.showStreams()
            isSearch = false
        }
        else subject.onNext(str)
    }

    fun initSearch() {
        subject
            .filter { str -> str.isNotEmpty() }
            .distinctUntilChanged()
            .debounce(1000, TimeUnit.MILLISECONDS)
            .switchMap { str ->
                val stream = chatDao.getStream(str)
                if (stream != null) {
                    Observable.just(stream)
                }
                else {
                    Observable.just(StreamDb(0, "", false))
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { stream ->
                    if (stream.nameChannel.isNotEmpty()) {
                        isSearch = true
                        viewState.showResultSearch(stream)
                    } else {
                        viewState.showIsEmptyResultSearch()
                    }
                },
                {
                    viewState.showError()
                }
            )
            .addTo(compositeDisposable)
    }

    fun resetSearchFlag() {
        isSearch = false
    }
}