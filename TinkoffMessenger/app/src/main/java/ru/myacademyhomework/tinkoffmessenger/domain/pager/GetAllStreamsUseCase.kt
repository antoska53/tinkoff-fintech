package ru.myacademyhomework.tinkoffmessenger.domain.pager

import javax.inject.Inject

class GetAllStreamsUseCase @Inject constructor(
    private val repository: PagerRepository
) {
    fun getAllStreams() = repository.getAllStreams()
}