package ru.myacademyhomework.tinkoffmessenger.streamfragment

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import ru.myacademyhomework.tinkoffmessenger.common.BasePresenter
import ru.myacademyhomework.tinkoffmessenger.database.ChatDao
import ru.myacademyhomework.tinkoffmessenger.network.Topic
import java.util.concurrent.TimeUnit

class StreamPresenter(private val chatDao: ChatDao) : BasePresenter<StreamView>() {

    private var isSearch = false
    private val subject = PublishSubject.create<String>()

    fun search(str: String) {
        subject.onNext(str)
    }

    fun initSearch() {
        subject
            .filter { str -> str.isNotEmpty() }
            .distinctUntilChanged()
            .debounce(1000, TimeUnit.MILLISECONDS)
            .switchMap { str ->
                val topic = chatDao.getTopic(str)
                if (topic != null) {
                    Observable.just(
                        Topic(
                            streamId = topic.streamId,
                            name = topic.nameTopic,
                            nameStream = topic.nameStream
                        )
                    )
                } else {
                    Observable.just(Topic(0, ""))
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { topic ->
                    if (topic.name.isNotEmpty()) {
                        isSearch = true
                        viewState.showResultSearch(topic)
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

    fun backPressed() {
        if (isSearch) {
        isSearch = false
            viewState.showStreams()
        } else {
            viewState.backPressed()
        }
    }

    fun resetSearchFlag(){
        isSearch = false
    }
}