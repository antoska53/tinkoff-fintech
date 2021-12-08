package ru.myacademyhomework.tinkoffmessenger.listeners

interface ChatMessageListener {
    fun itemLongClicked(idMessage: Long, position: Int): Boolean
}