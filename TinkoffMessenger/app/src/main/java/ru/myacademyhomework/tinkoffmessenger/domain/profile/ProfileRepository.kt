package ru.myacademyhomework.tinkoffmessenger.domain.profile

import io.reactivex.Single


interface ProfileRepository {
    fun getUserInfo(userId: Int): Single<UserInfo>
    fun getUserStatus(userId: Int): Single<UserStatus>

}