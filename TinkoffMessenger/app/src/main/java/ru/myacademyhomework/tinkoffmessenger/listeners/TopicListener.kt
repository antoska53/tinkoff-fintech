package ru.myacademyhomework.tinkoffmessenger.listeners

import ru.myacademyhomework.tinkoffmessenger.network.Topic

fun interface TopicListener {
    fun onClickStream(topic: Topic)
}