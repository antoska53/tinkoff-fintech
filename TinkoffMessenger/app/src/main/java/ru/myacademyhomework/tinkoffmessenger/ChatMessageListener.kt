package ru.myacademyhomework.tinkoffmessenger

interface ChatMessageListener {
    fun itemLongClicked(idMessage: Int, position: Int): Boolean
}