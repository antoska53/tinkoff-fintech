package ru.myacademyhomework.tinkoffmessenger.domain.people

import javax.inject.Inject

class InitSearchUseCase @Inject constructor(
    private val repository: PeopleRepository
) {
    fun initSearch() = repository.initSearch()
}