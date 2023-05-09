package ru.myacademyhomework.tinkoffmessenger.domain.people

import io.reactivex.Single
import ru.myacademyhomework.tinkoffmessenger.data.network.model.UsersResponse
import javax.inject.Inject

class GetAllUserFromNetworkUseCase @Inject constructor(
    private val repository: PeopleRepository
) {
    fun getAllUser(): Single<UsersResponse> = repository.getAllUserFromNetwork()
}