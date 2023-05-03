package ru.myacademyhomework.tinkoffmessenger.data.people

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import ru.myacademyhomework.tinkoffmessenger.data.database.ChatDao
import ru.myacademyhomework.tinkoffmessenger.data.mapper.UserMapper
import ru.myacademyhomework.tinkoffmessenger.data.network.model.UsersResponse
import ru.myacademyhomework.tinkoffmessenger.di.ApiClient
import ru.myacademyhomework.tinkoffmessenger.di.people.PeopleScope
import ru.myacademyhomework.tinkoffmessenger.domain.people.PeopleRepository
import ru.myacademyhomework.tinkoffmessenger.domain.profile.UserInfo
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@PeopleScope
class PeopleRepositoryImpl @Inject constructor(
    private val apiClient: ApiClient,
    private val chatDao: ChatDao,
    private val mapper: UserMapper,
) : PeopleRepository {

    private val subject = PublishSubject.create<String>()

    override fun getAllUserFromDb(): Flowable<List<UserInfo>> {
        return chatDao.getAllUsers()
            .map {
                it.map { userDb ->
                    mapper.mapDbModelToEntity(userDb)
                }
            }
    }

    override fun getAllUserFromNetwork(): Single<UsersResponse> {
       return apiClient.chatApi.getAllUsers()
            .subscribeOn(Schedulers.io())
            .doOnSuccess {
                chatDao.insertUsers(it.users.map { user ->
                    mapper.mapDtoToDbModel(user)
                })
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun search(str: String) {
        subject.onNext(str)
    }

    override fun initSearch(): Observable<UserInfo> {
        return subject
            .filter { str -> str.isNotEmpty() }
            .distinctUntilChanged()
            .debounce(1000, TimeUnit.MILLISECONDS)
            .switchMap { str ->
                val userDb = chatDao.getUserForSearch(str)
                if (userDb != null) {
                    Observable.just(
                        mapper.mapDbModelToEntity(userDb)
                    )
                }
                else {
                    Observable.just(UserInfo("", "", "", 0))
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}