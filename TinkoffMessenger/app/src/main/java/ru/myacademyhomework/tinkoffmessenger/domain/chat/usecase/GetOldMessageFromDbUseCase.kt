package ru.myacademyhomework.tinkoffmessenger.domain.chat.usecase

import ru.myacademyhomework.tinkoffmessenger.domain.chat.ChatRepository
import javax.inject.Inject

class GetOldMessageFromDbUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    fun getOldMessageFromDb(nameTopic: String, firstMessageId: Long) =
        repository.getOldMessageFromDb(nameTopic, firstMessageId)
}