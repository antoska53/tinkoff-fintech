package ru.myacademyhomework.tinkoffmessenger.streamfragment

import moxy.viewstate.strategy.alias.AddToEnd
import moxy.viewstate.strategy.alias.Skip
import ru.myacademyhomework.tinkoffmessenger.common.BaseView
import ru.myacademyhomework.tinkoffmessenger.database.StreamDb

interface StreamView : BaseView {

    @AddToEnd
    fun showResultSearch(stream: StreamDb)

    @AddToEnd
    fun showIsEmptyResultSearch()

    @AddToEnd
    fun showStreams()

    @Skip
    fun backPressed()
}