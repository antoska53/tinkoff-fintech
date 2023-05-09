package ru.myacademyhomework.tinkoffmessenger.domain.people

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import ru.myacademyhomework.tinkoffmessenger.data.network.model.UsersResponse
import ru.myacademyhomework.tinkoffmessenger.domain.profile.UserInfo

interface PeopleRepository {
    fun getAllUserFromDb(): Flowable<List<UserInfo>>
    fun getAllUserFromNetwork(): Single<UsersResponse>
    fun search(str: String)
    fun initSearch(): Observable<UserInfo>
}