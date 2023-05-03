package ru.myacademyhomework.tinkoffmessenger.domain.profile

import io.reactivex.Single
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    fun getUserInfo(userId: Int) : Single<UserInfo> = repository.getUserInfo(userId)
}