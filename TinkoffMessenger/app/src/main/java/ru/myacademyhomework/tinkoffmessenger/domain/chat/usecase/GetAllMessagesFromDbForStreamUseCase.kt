package ru.myacademyhomework.tinkoffmessenger.domain.chat.usecase

import ru.myacademyhomework.tinkoffmessenger.domain.chat.ChatRepository
import javax.inject.Inject

class GetAllMessagesFromDbForStreamUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    fun getAllMessagesFromDbForStream(nameStream: String) =
        repository.getAllMessagesFromDbForStream(nameStream)
}