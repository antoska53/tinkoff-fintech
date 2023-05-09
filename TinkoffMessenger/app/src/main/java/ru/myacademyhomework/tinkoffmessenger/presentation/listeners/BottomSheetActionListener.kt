package ru.myacademyhomework.tinkoffmessenger.presentation.listeners

import ru.myacademyhomework.tinkoffmessenger.domain.util.ActionBottomSheet

fun interface BottomSheetActionListener {
    fun itemActionClicked(action: ActionBottomSheet, idMessage: Long, nameTopic: String, message: String, positionMessage: Int)
}