package ru.myacademyhomework.tinkoffmessenger.domain.chat.usecase

import ru.myacademyhomework.tinkoffmessenger.domain.chat.ChatRepository
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    fun getMessages(anchor: String, nameStream: String, nameTopic: String) =
        repository.getMessages(anchor, nameStream, nameTopic)
}