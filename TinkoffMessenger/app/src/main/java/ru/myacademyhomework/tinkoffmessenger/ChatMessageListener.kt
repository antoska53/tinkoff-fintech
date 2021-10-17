package ru.myacademyhomework.tinkoffmessenger

interface ChatMessageListener {
    fun itemLongClicked(positionMessage: Int): Boolean
}