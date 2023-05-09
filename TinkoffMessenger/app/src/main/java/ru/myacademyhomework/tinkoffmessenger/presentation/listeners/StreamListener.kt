package ru.myacademyhomework.tinkoffmessenger.presentation.listeners

import ru.myacademyhomework.tinkoffmessenger.data.Stream
import ru.myacademyhomework.tinkoffmessenger.data.network.model.TopicDto

interface StreamListener {
    fun itemStreamArrowClicked(topics: List<TopicDto>, position: Int, isSelected: Boolean)
    fun itemStreamClicked(stream: Stream)
}