package ru.myacademyhomework.tinkoffmessenger.domain.chat.usecase

import ru.myacademyhomework.tinkoffmessenger.domain.chat.UserMessage
import ru.myacademyhomework.tinkoffmessenger.domain.chat.ChatRepository
import javax.inject.Inject

class SetupListMessageForStreamUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    fun setupListMessageForStream(messages: List<UserMessage>) =
        repository.setupListMessageForStream(messages)
}