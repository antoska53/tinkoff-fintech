package ru.myacademyhomework.tinkoffmessenger.domain.chat.usecase

import ru.myacademyhomework.tinkoffmessenger.domain.chat.ChatRepository
import javax.inject.Inject

class GetOwnUserUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    fun getOwnUser() = repository.getOwnUser()
}