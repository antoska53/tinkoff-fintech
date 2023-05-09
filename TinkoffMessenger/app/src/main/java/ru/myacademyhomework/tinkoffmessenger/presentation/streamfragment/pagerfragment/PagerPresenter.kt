package ru.myacademyhomework.tinkoffmessenger.presentation.streamfragment.pagerfragment

import io.reactivex.rxkotlin.addTo
import ru.myacademyhomework.tinkoffmessenger.presentation.common.BasePresenter
import ru.myacademyhomework.tinkoffmessenger.domain.pager.Stream
import ru.myacademyhomework.tinkoffmessenger.di.pager.PagerScope
import ru.myacademyhomework.tinkoffmessenger.data.network.model.TopicDto
import ru.myacademyhomework.tinkoffmessenger.domain.pager.GetAllStreamsFromDbUseCase
import ru.myacademyhomework.tinkoffmessenger.domain.pager.GetAllStreamsUseCase
import ru.myacademyhomework.tinkoffmessenger.domain.pager.GetSubscribeStreamsFromDbUseCase
import ru.myacademyhomework.tinkoffmessenger.domain.pager.GetSubscribeStreamsUseCase
import ru.myacademyhomework.tinkoffmessenger.domain.pager.GetTopicsFromDbUseCase
import ru.myacademyhomework.tinkoffmessenger.domain.pager.Topic
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
        getTopicsFromDbUseCase.getTopicsFromDb(streamName)
            .subscribe({
                viewState.setDataToRecycler(listOf(it))
            }, {

            })
            .addTo(compositeDisposable)
    }

    fun getSubscribeStreams() {
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