package ru.myacademyhomework.tinkoffmessenger.domain.newstream

import javax.inject.Inject

class CreateNewStreamUseCase @Inject constructor(
    private val repository: NewStreamRepository
) {
    fun createNewStream(nameStream: String, description: String) = repository.createNewStream(nameStream, description)
}