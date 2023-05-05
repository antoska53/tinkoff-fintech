package ru.myacademyhomework.tinkoffmessenger.domain.chat

import ru.myacademyhomework.tinkoffmessenger.data.network.model.UserMessage
import javax.inject.Inject

class SetupListMessageForStreamUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    fun setupListMessageForStream(messages: List<UserMessage>) =
        repository.setupListMessageForStream(messages)
}