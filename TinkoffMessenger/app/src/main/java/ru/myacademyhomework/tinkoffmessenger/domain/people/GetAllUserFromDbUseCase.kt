package ru.myacademyhomework.tinkoffmessenger.domain.people

import io.reactivex.Flowable
import ru.myacademyhomework.tinkoffmessenger.domain.profile.UserInfo
import javax.inject.Inject

class GetAllUserFromDbUseCase @Inject constructor(
    private val repository: PeopleRepository
) {
    fun getAllUser(): Flowable<List<UserInfo>> = repository.getAllUserFromDb()
}