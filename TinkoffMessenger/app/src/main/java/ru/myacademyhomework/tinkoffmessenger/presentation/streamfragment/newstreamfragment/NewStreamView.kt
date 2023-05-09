package ru.myacademyhomework.tinkoffmessenger.presentation.streamfragment.newstreamfragment

import moxy.viewstate.strategy.alias.Skip
import ru.myacademyhomework.tinkoffmessenger.presentation.common.BaseView

interface NewStreamView :BaseView {
    @Skip
    fun showSuccessCreate()

    @Skip
    fun showEmptyNameDescription()
}