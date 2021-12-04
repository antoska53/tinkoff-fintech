package ru.myacademyhomework.tinkoffmessenger

import moxy.InjectViewState
import ru.myacademyhomework.tinkoffmessenger.common.BasePresenter

@InjectViewState
class FlowFragmentPresenter : BasePresenter<FlowFragmentView>() {

    fun loadFragment(itemId: Int, toBackstack: Boolean) {
        viewState.loadFragment(itemId, toBackstack)
    }
}