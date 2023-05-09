package ru.myacademyhomework.tinkoffmessenger.domain.stream

import javax.inject.Inject

class InitSearchUseCase @Inject constructor(
    private val repository: StreamRepository
) {
    fun initSearch() = repository.initSearch()
}