package ru.myacademyhomework.tinkoffmessenger.presentation.streamfragment

import moxy.viewstate.strategy.alias.AddToEnd
import ru.myacademyhomework.tinkoffmessenger.presentation.common.BaseView
import ru.myacademyhomework.tinkoffmessenger.domain.stream.StreamInfo

interface StreamView : BaseView {

    @AddToEnd
    fun showResultSearch(stream: StreamInfo)

    @AddToEnd
    fun showIsEmptyResultSearch()

    @AddToEnd
    fun showStreams()

}