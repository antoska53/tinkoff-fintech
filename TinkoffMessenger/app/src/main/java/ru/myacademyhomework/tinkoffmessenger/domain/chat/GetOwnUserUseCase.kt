package ru.myacademyhomework.tinkoffmessenger.domain.chat

import javax.inject.Inject

class GetOwnUserUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    fun getOwnUser() = repository.getOwnUser()
}