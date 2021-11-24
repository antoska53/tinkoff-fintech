package ru.myacademyhomework.tinkoffmessenger

interface ChatMessageListener {
    fun itemLongClicked(idMessage: Long, position: Int): Boolean
}