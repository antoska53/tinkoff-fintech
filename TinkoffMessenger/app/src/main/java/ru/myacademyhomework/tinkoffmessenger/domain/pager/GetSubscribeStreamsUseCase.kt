package ru.myacademyhomework.tinkoffmessenger.domain.pager

import javax.inject.Inject

class GetSubscribeStreamsUseCase @Inject constructor(
    private val repository: PagerRepository
) {
    fun getSubscribeStreams() = repository.getSubscribeStreams()
}