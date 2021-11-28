package ru.myacademyhomework.tinkoffmessenger.streamfragment.pagerfragments

import moxy.viewstate.strategy.alias.AddToEnd
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.Skip
import ru.myacademyhomework.tinkoffmessenger.common.BaseView
import ru.myacademyhomework.tinkoffmessenger.data.Stream
import ru.myacademyhomework.tinkoffmessenger.network.Topic

interface PagerView : BaseView {

    @Skip
    fun showErrorUpdateData()

    @AddToEnd
    fun openChatTopic(topic: Topic)

    @AddToEnd
    fun setDataToRecycler(listStream: List<Stream>)
}