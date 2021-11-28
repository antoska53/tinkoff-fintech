package ru.myacademyhomework.tinkoffmessenger.listeners

import ru.myacademyhomework.tinkoffmessenger.network.Topic

fun interface StreamListener {
    fun itemChannelClicked(streams: List<Topic>, position: Int, isSelected: Boolean)
}