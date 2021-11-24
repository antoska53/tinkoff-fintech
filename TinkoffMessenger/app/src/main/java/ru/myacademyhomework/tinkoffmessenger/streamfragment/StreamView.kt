package ru.myacademyhomework.tinkoffmessenger.streamfragment

import moxy.viewstate.strategy.alias.AddToEndSingle
import ru.myacademyhomework.tinkoffmessenger.common.BaseView
import ru.myacademyhomework.tinkoffmessenger.network.Topic

interface StreamView : BaseView {

    @AddToEndSingle
    fun showResultSearch(topic: Topic)

    @AddToEndSingle
    fun showIsEmptyResultSearch()
}