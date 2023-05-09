package ru.myacademyhomework.tinkoffmessenger.presentation

import moxy.viewstate.strategy.alias.AddToEnd
import ru.myacademyhomework.tinkoffmessenger.presentation.common.BaseView

interface FlowFragmentView : BaseView {

    @AddToEnd
    fun loadFragment(itemId: Int, toBackstack: Boolean)
}