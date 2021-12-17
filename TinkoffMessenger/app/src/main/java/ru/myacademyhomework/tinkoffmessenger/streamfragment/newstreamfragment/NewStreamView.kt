package ru.myacademyhomework.tinkoffmessenger.streamfragment.newstreamfragment

import moxy.viewstate.strategy.alias.Skip
import ru.myacademyhomework.tinkoffmessenger.common.BaseView

interface NewStreamView :BaseView {
    @Skip
    fun showSuccessCreate()
}