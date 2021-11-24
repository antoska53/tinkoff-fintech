package ru.myacademyhomework.tinkoffmessenger.peoplefragment

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import ru.myacademyhomework.tinkoffmessenger.common.BasePresenter
import ru.myacademyhomework.tinkoffmessenger.database.ChatDao
import ru.myacademyhomework.tinkoffmessenger.database.UserDb
import ru.myacademyhomework.tinkoffmessenger.network.RetrofitModule
import ru.myacademyhomework.tinkoffmessenger.network.User

class PeoplePresenter(private val chatDao: ChatDao) : BasePresenter<PeopleView>() {

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
        RetrofitModule.chatApi.getAllUsers()
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