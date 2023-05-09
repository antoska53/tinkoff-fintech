package ru.myacademyhomework.tinkoffmessenger.domain.chat.usecase

import ru.myacademyhomework.tinkoffmessenger.domain.chat.ChatRepository
import javax.inject.Inject

class DeleteMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    fun deleteMessage(idMessage: Long) = repository.deleteMessage(idMessage)
}