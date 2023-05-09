package ru.myacademyhomework.tinkoffmessenger.domain.stream

import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val repository: StreamRepository
) {
    fun search(str: String) = repository.search(str)
}