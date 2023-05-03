package ru.myacademyhomework.tinkoffmessenger.listeners

import ru.myacademyhomework.tinkoffmessenger.data.Stream
import ru.myacademyhomework.tinkoffmessenger.data.network.model.Topic

interface StreamListener {
    fun itemStreamArrowClicked(topics: List<Topic>, position: Int, isSelected: Boolean)
    fun itemStreamClicked(stream: Stream)
}