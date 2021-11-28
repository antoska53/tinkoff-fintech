package ru.myacademyhomework.tinkoffmessenger.listeners

fun interface BottomSheetListener {
    fun itemEmojiClicked(emoji: String, idMessage: Long, position: Int)
}