package ru.myacademyhomework.tinkoffmessenger.domain.chat

import javax.inject.Inject

class GetMessagesForStreamUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    fun getMessagesForStream(anchor: String, nameStream: String) =
        repository.getMessagesForStream(anchor, nameStream)
}