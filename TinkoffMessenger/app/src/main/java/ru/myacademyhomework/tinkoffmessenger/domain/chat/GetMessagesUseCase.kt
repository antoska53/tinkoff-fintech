package ru.myacademyhomework.tinkoffmessenger.domain.chat

import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    fun getMessages(anchor: String) = repository.getMessages(anchor)
}