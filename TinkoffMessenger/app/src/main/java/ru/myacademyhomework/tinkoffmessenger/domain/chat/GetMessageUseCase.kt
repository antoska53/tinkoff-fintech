package ru.myacademyhomework.tinkoffmessenger.domain.chat

import javax.inject.Inject

class GetMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    fun getMessage(messageId: Long, position: Int, nameTopic: String?) =
        repository.getMessage(messageId, position, nameTopic)
}