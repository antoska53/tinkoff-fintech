package ru.myacademyhomework.tinkoffmessenger.presentation

import moxy.InjectViewState
import ru.myacademyhomework.tinkoffmessenger.presentation.common.BasePresenter

@InjectViewState
class FlowFragmentPresenter : BasePresenter<FlowFragmentView>() {

    fun loadFragment(itemId: Int, toBackstack: Boolean) {
        viewState.loadFragment(itemId, toBackstack)
    }
}