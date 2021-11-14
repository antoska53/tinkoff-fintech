package ru.myacademyhomework.tinkoffmessenger

fun interface BottomSheetListener {
    fun itemEmojiClicked(emoji: String, idMessage: Long, position: Int)
}