package ru.myacademyhomework.tinkoffmessenger.domain.chat.usecase

import ru.myacademyhomework.tinkoffmessenger.domain.chat.ChatRepository
import javax.inject.Inject

class ShowPopupMenuUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    fun showPopupMenu(nameStream: String) = repository.showPopupMenu(nameStream)
}