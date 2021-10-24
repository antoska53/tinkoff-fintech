package ru.myacademyhomework.tinkoffmessenger

interface ChatMessageListener {
    fun itemLongClicked(idMessage: Int): Boolean
}