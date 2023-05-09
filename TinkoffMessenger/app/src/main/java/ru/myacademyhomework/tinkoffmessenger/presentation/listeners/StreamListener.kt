package ru.myacademyhomework.tinkoffmessenger.presentation.listeners

import ru.myacademyhomework.tinkoffmessenger.domain.pager.Stream
import ru.myacademyhomework.tinkoffmessenger.data.network.model.TopicDto
import ru.myacademyhomework.tinkoffmessenger.domain.pager.Topic

interface StreamListener {
    fun itemStreamArrowClicked(topics: List<Topic>, position: Int, isSelected: Boolean)
    fun itemStreamClicked(stream: Stream)
}