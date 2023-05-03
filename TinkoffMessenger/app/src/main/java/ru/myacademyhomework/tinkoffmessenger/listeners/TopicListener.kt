package ru.myacademyhomework.tinkoffmessenger.listeners

import ru.myacademyhomework.tinkoffmessenger.data.network.model.Topic

fun interface TopicListener {
    fun onClickStream(topic: Topic)
}