package ru.myacademyhomework.tinkoffmessenger.listeners

import ru.myacademyhomework.tinkoffmessenger.data.network.model.TopicDto

fun interface TopicListener {
    fun onClickStream(topic: TopicDto)
}