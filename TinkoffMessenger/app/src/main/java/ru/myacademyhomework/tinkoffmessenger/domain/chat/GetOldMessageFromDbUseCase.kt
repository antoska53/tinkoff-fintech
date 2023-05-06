package ru.myacademyhomework.tinkoffmessenger.domain.chat

import javax.inject.Inject

class GetOldMessageFromDbUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    fun getOldMessageFromDb(nameTopic: String, firstMessageId: Long) =
        repository.getOldMessageFromDb(nameTopic, firstMessageId)
}