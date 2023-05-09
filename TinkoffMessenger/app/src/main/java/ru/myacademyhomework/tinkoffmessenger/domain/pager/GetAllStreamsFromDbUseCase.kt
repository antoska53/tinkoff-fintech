package ru.myacademyhomework.tinkoffmessenger.domain.pager

import javax.inject.Inject

class GetAllStreamsFromDbUseCase @Inject constructor(
    private val repository: PagerRepository
) {
    fun getAllStreamsFromDb() = repository.getAllStreamsFromDb()
}