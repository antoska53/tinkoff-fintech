package ru.myacademyhomework.tinkoffmessenger.domain.chat.usecase

import ru.myacademyhomework.tinkoffmessenger.domain.chat.ChatRepository
import javax.inject.Inject

class GetMessagesForStreamUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    fun getMessagesForStream(anchor: String, nameStream: String) =
        repository.getMessagesForStream(anchor, nameStream)
}