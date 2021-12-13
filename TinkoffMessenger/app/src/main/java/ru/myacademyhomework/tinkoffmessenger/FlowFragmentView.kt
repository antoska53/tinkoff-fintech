package ru.myacademyhomework.tinkoffmessenger

import moxy.viewstate.strategy.alias.AddToEnd
import ru.myacademyhomework.tinkoffmessenger.common.BaseView

interface FlowFragmentView : BaseView {

    @AddToEnd
    fun loadFragment(itemId: Int, toBackstack: Boolean)
}