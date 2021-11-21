package ru.myacademyhomework.tinkoffmessenger.profilefragment

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import ru.myacademyhomework.tinkoffmessenger.common.BasePresenter
import ru.myacademyhomework.tinkoffmessenger.database.ChatDao
import ru.myacademyhomework.tinkoffmessenger.network.RetrofitModule
import ru.myacademyhomework.tinkoffmessenger.network.User

class ProfilePresenter(
    private val view: ProfileView,
    private val chatDao: ChatDao,
    private val userId: Int
): BasePresenter() {

    fun getUserFromDb() {
        chatDao.getUser(userId)
            .map {
                User(
                    avatarURL = it.avatarURL,
                    email = it.email,
                    fullName = it.fullName,
                    userID = it.userID
                )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { user ->
                    view.showUserProfile(user)
                }, {

                }
            )
            .addTo(compositeDisposable)
    }

    fun getOwnUser() {
        RetrofitModule.chatApi.getOwnUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                view.showRefresh()
            }
            .subscribe({ user ->
                view.showUserProfile(user)
                view.hideRefresh()
            }, {
                view.showError()
            })
            .addTo(compositeDisposable)
    }

    fun getStatus() {
        RetrofitModule.chatApi.getUserPresence(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.showStatus(it.presence.userStatus.status)
            }, {
                view.showErrorLoadStatus()
            })
            .addTo(compositeDisposable)
    }
}