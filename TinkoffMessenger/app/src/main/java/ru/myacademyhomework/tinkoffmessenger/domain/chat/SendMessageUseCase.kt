package ru.myacademyhomework.tinkoffmessenger.domain.chat

import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    fun sendMessage(message: String, nameTopic: String, nameStream: String) =
        repository.sendMessage(message, nameTopic, nameStream)
}