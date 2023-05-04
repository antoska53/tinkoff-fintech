package ru.myacademyhomework.tinkoffmessenger.presentation.streamfragment

import io.reactivex.rxkotlin.addTo
import ru.myacademyhomework.tinkoffmessenger.common.BasePresenter
import ru.myacademyhomework.tinkoffmessenger.di.stream.StreamScope
import ru.myacademyhomework.tinkoffmessenger.domain.stream.InitSearchUseCase
import ru.myacademyhomework.tinkoffmessenger.domain.stream.SearchUseCase
import javax.inject.Inject

@StreamScope
class StreamPresenter @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val initSearchUseCase: InitSearchUseCase
) :
    BasePresenter<StreamView>() {

    private var isSearch = false

    fun search(str: String) {
        if (str.isEmpty() && isSearch) {
            viewState.showStreams()
            isSearch = false
        } else searchUseCase.search(str)
    }

    fun initSearch() {
        initSearchUseCase.initSearch()
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