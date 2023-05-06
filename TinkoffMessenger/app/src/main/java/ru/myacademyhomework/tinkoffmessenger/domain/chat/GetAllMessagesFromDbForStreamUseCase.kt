package ru.myacademyhomework.tinkoffmessenger.domain.chat

import javax.inject.Inject

class GetAllMessagesFromDbForStreamUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    fun getAllMessagesFromDbForStream(nameStream: String) =
        repository.getAllMessagesFromDbForStream(nameStream)
}