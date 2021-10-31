package ru.myacademyhomework.tinkoffmessenger

fun interface ChannelListener {
    fun itemChannelClicked(streams: List<String>, position: Int, isSelected: Boolean)
}