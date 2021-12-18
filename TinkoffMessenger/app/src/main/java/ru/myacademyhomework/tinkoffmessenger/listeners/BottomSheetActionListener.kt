package ru.myacademyhomework.tinkoffmessenger.listeners

fun interface BottomSheetActionListener {
    fun itemActionClicked(nameAction: String, idMessage: Long, nameTopic: String, message: String, positionMessage: Int)
}