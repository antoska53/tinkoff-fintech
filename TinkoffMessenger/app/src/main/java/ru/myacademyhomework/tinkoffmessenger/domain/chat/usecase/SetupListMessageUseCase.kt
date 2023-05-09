package ru.myacademyhomework.tinkoffmessenger.domain.chat.usecase

import ru.myacademyhomework.tinkoffmessenger.domain.chat.UserMessage
import ru.myacademyhomework.tinkoffmessenger.domain.chat.ChatRepository
import javax.inject.Inject

class SetupListMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    fun setupListMessage(messages: List<UserMessage>) = repository.setupListMessage(messages)
}