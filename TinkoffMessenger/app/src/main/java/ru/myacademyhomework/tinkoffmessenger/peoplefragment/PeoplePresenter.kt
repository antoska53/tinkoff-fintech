package ru.myacademyhomework.tinkoffmessenger.peoplefragment

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import moxy.viewstate.strategy.alias.Skip
import ru.myacademyhomework.tinkoffmessenger.common.BasePresenter
import ru.myacademyhomework.tinkoffmessenger.database.ChatDao
import ru.myacademyhomework.tinkoffmessenger.database.UserDb
import ru.myacademyhomework.tinkoffmessenger.di.ApiClient
import ru.myacademyhomework.tinkoffmessenger.di.people.PeopleScope
import ru.myacademyhomework.tinkoffmessenger.network.User
import javax.inject.Inject

@PeopleScope
class PeoplePresenter @Inject constructor(
    private val chatDao: ChatDao,
    private val apiClient: ApiClient
) :
    BasePresenter<PeopleView>() {

    @Skip
    fun openProfile(userId: Int) {
        viewState.openProfileFragment(userId)
    }

    fun getAllUsersFromDb() {
        chatDao.getAllUsers()
            .map {
                it.map { userDb ->
                    User(
                        avatarURL = userDb.avatarURL,
                        email = userDb.email,
                        fullName = userDb.fullName,
                        userID = userDb.userID
                    )
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewState.updateRecycler(it)
            }, {

            })
            .addTo(compositeDisposable)
    }

    fun getAllUsers() {
        apiClient.chatApi.getAllUsers()
            .subscribeOn(Schedulers.io())
            .doOnSuccess {
                chatDao.insertUsers(it.users.map { user ->
                    UserDb(
                        avatarURL = user.avatarURL,
                        email = user.email,
                        fullName = user.fullName,
                        userID = user.userID,
                        isOwn = false
                    )
                })
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
            .addTo(compositeDisposable)
    }
}