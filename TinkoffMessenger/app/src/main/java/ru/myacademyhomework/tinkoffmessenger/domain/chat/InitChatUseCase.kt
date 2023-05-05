package ru.myacademyhomework.tinkoffmessenger.domain.chat

import javax.inject.Inject

class InitChatUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    fun initChat() = repository.initChat()
}