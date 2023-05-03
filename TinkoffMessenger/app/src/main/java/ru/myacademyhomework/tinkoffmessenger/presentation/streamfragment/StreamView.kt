package ru.myacademyhomework.tinkoffmessenger.presentation.streamfragment

import moxy.viewstate.strategy.alias.AddToEnd
import ru.myacademyhomework.tinkoffmessenger.common.BaseView
import ru.myacademyhomework.tinkoffmessenger.data.database.model.StreamDb

interface StreamView : BaseView {

    @AddToEnd
    fun showResultSearch(stream: StreamDb)

    @AddToEnd
    fun showIsEmptyResultSearch()

    @AddToEnd
    fun showStreams()

}