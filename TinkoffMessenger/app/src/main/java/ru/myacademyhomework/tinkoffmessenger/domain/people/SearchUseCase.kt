package ru.myacademyhomework.tinkoffmessenger.domain.people

import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val repository: PeopleRepository
) {
    fun search(str: String) = repository.search(str)
}