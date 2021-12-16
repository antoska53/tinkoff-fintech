package ru.myacademyhomework.tinkoffmessenger.listeners

fun interface BottomSheetListener {
    fun itemEmojiClicked(emoji: String, idMessage: Long, nameTopic: String, position: Int)
}