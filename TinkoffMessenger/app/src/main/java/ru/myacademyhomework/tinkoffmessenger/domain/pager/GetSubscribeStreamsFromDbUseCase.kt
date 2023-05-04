package ru.myacademyhomework.tinkoffmessenger.domain.pager

import javax.inject.Inject

class GetSubscribeStreamsFromDbUseCase @Inject constructor(
    private val repository: PagerRepository
) {
    fun getSubscribeStreamsFromDb() = repository.getSubscribeStreamsFromDb()
}