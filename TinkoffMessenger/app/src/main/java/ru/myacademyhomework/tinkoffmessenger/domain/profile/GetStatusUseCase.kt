package ru.myacademyhomework.tinkoffmessenger.domain.profile

import io.reactivex.Single
import javax.inject.Inject

class GetStatusUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    fun getStatus(userId: Int): Single<UserStatus> = repository.getUserStatus(userId)
}