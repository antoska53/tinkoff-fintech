package ru.myacademyhomework.tinkoffmessenger.presentation.listeners

import ru.myacademyhomework.tinkoffmessenger.data.network.model.TopicDto
import ru.myacademyhomework.tinkoffmessenger.domain.pager.Topic

fun interface TopicListener {
    fun onClickStream(topic: Topic)
}