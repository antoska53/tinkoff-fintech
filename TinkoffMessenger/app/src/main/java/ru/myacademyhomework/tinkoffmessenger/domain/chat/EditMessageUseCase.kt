package ru.myacademyhomework.tinkoffmessenger.domain.chat

import javax.inject.Inject

class EditMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    fun editMessage(messageId: Long, nameTopic: String, message: String) =
        repository.editMessage(messageId, nameTopic, message)
}