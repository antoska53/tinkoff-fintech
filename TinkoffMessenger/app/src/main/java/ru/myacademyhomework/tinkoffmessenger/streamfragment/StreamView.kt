package ru.myacademyhomework.tinkoffmessenger.streamfragment

import ru.myacademyhomework.tinkoffmessenger.common.BaseView
import ru.myacademyhomework.tinkoffmessenger.network.Topic

interface StreamView: BaseView {
    fun showResultSearch(topic: Topic)
    fun showIsEmptyResultSearch()
}