package ru.myacademyhomework.tinkoffmessenger.presentation.streamfragment.pagerfragment

import moxy.viewstate.strategy.alias.AddToEnd
import moxy.viewstate.strategy.alias.Skip
import ru.myacademyhomework.tinkoffmessenger.common.BaseView
import ru.myacademyhomework.tinkoffmessenger.data.Stream
import ru.myacademyhomework.tinkoffmessenger.data.network.model.Topic

interface PagerView : BaseView {

    @Skip
    fun showErrorUpdateData()

    @Skip
    fun openChatTopic(topic: Topic)

    @Skip
    fun openChatStream(stream: Stream)

    @AddToEnd
    fun setDataToRecycler(listStream: List<Stream>)

    @Skip
    fun openNewStreamFragment()
}