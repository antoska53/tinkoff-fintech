package ru.myacademyhomework.tinkoffmessenger.domain.pager

import javax.inject.Inject

class GetTopicsFromDbUseCase @Inject constructor(
    private val repository: PagerRepository
) {
    fun getTopicsFromDb(streamName: String) = repository.getTopicsFromDb(streamName)
}