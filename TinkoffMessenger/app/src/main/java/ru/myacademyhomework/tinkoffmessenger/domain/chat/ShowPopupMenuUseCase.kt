package ru.myacademyhomework.tinkoffmessenger.domain.chat

import javax.inject.Inject

class ShowPopupMenuUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    fun showPopupMenu(nameStream: String) = repository.showPopupMenu(nameStream)
}