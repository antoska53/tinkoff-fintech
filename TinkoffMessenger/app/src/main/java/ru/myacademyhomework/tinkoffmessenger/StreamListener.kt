package ru.myacademyhomework.tinkoffmessenger

import ru.myacademyhomework.tinkoffmessenger.data.ItemStream

fun interface StreamListener {
    fun onClickStream(stream: ItemStream)
}