package ru.myacademyhomework.tinkoffmessenger

fun interface BottomSheetListener {
    fun itemEmojiClicked(emoji: String, idMessage: Int, position: Int)
}