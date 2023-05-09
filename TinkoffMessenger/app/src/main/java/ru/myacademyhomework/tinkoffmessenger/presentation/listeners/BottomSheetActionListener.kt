package ru.myacademyhomework.tinkoffmessenger.presentation.listeners

import ru.myacademyhomework.tinkoffmessenger.data.ActionBottomSheet

fun interface BottomSheetActionListener {
    fun itemActionClicked(action: ActionBottomSheet, idMessage: Long, nameTopic: String, message: String, positionMessage: Int)
}