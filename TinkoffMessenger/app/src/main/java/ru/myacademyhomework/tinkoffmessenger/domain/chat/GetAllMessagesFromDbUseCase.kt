package ru.myacademyhomework.tinkoffmessenger.domain.chat

import javax.inject.Inject

class GetAllMessagesFromDbUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    fun getAllMessagesFromDb(nameTopic: String, nameStream: String) =
        repository.getAllMessagesFromDb(nameTopic, nameStream)
}