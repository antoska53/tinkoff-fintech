package ru.myacademyhomework.tinkoffmessenger.presentation.listeners

fun interface BottomSheetListener {
    fun itemEmojiClicked(emoji: String, idMessage: Long, nameTopic: String, position: Int)
}