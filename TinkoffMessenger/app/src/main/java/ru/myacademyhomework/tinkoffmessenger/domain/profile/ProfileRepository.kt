package ru.myacademyhomework.tinkoffmessenger.domain.profile

import io.reactivex.Single
import io.reactivex.subjects.PublishSubject


interface ProfileRepository {
    fun getUserInfo(userId: Int): Single<UserInfo>
    fun getUserStatus(userId: Int): Single<UserStatus>

}