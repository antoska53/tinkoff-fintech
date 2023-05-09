package ru.myacademyhomework.tinkoffmessenger.domain.chat.usecase

import ru.myacademyhomework.tinkoffmessenger.domain.chat.ChatRepository
import javax.inject.Inject

class InitChatUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    fun initChat() = repository.initChat()
}