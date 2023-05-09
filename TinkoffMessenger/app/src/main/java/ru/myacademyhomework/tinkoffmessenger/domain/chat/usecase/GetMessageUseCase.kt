package ru.myacademyhomework.tinkoffmessenger.domain.chat.usecase

import ru.myacademyhomework.tinkoffmessenger.domain.chat.ChatRepository
import javax.inject.Inject

class GetMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    fun getMessage(messageId: Long, position: Int, nameTopic: String, nameStream: String?) =
        repository.getMessage(messageId, position, nameTopic, nameStream)
}