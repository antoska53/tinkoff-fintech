package ru.myacademyhomework.tinkoffmessenger

import ru.myacademyhomework.tinkoffmessenger.data.ItemStream

fun interface ChannelListener {
    fun itemChannelClicked(streams: List<ItemStream>, position: Int, isSelected: Boolean)
}