package ru.myacademyhomework.tinkoffmessenger

import ru.myacademyhomework.tinkoffmessenger.network.Topic

fun interface TopicListener {
    fun onClickStream(topic: Topic)
}