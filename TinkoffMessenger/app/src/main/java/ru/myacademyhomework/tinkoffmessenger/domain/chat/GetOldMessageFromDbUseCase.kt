package ru.myacademyhomework.tinkoffmessenger.domain.chat

import javax.inject.Inject

class GetOldMessageFromDbUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    fun getOldMessageFromDb() = repository.getOldMessageFromDb()
}