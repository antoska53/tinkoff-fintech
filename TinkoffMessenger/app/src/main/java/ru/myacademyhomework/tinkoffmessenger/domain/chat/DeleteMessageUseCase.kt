package ru.myacademyhomework.tinkoffmessenger.domain.chat

import javax.inject.Inject

class DeleteMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    fun deleteMessage(idMessage: Long) = repository.deleteMessage(idMessage)
}