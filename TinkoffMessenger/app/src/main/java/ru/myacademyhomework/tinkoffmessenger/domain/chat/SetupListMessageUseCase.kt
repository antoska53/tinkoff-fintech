package ru.myacademyhomework.tinkoffmessenger.domain.chat

import ru.myacademyhomework.tinkoffmessenger.data.network.model.UserMessage
import javax.inject.Inject

class SetupListMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    fun setupListMessage(messages: List<UserMessage>) = repository.setupListMessage(messages)
}