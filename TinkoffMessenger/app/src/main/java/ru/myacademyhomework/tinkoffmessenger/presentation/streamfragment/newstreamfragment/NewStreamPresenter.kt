package ru.myacademyhomework.tinkoffmessenger.presentation.streamfragment.newstreamfragment

import io.reactivex.rxkotlin.addTo
import ru.myacademyhomework.tinkoffmessenger.presentation.common.BasePresenter
import ru.myacademyhomework.tinkoffmessenger.di.newstream.NewStreamScope
import ru.myacademyhomework.tinkoffmessenger.domain.newstream.CreateNewStreamUseCase
import javax.inject.Inject

@NewStreamScope
class NewStreamPresenter @Inject constructor(
    private val createNewStreamUseCase: CreateNewStreamUseCase
) : BasePresenter<NewStreamView>() {

    fun createNewStream(nameStream: String, description: String) {
        if (nameStream.isNotEmpty() && description.isNotEmpty()) {
            createNewStreamUseCase.createNewStream(nameStream, description)
                .subscribe({
                    viewState.showSuccessCreate()
                }, {
                    viewState.showError()
                })
                .addTo(compositeDisposable)
        } else {
            viewState.showEmptyNameDescription()
        }
    }
}