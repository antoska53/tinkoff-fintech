package ru.myacademyhomework.tinkoffmessenger.streamfragment.pagerfragments

import moxy.viewstate.strategy.alias.AddToEndSingle
import ru.myacademyhomework.tinkoffmessenger.common.BaseView
import ru.myacademyhomework.tinkoffmessenger.data.Stream
import ru.myacademyhomework.tinkoffmessenger.network.Topic

interface PagerView : BaseView {

    @AddToEndSingle
    fun showErrorUpdateData()

    @AddToEndSingle
    fun openChatTopic(topic: Topic)

    @AddToEndSingle
    fun setDataToRecycler(listStream: List<Stream>)
}