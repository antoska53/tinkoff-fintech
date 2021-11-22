package ru.myacademyhomework.tinkoffmessenger.profilefragment

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import ru.myacademyhomework.tinkoffmessenger.common.BasePresenter
import ru.myacademyhomework.tinkoffmessenger.database.ChatDao
import ru.myacademyhomework.tinkoffmessenger.network.RetrofitModule
import ru.myacademyhomework.tinkoffmessenger.network.User

@InjectViewState
class ProfilePresenter(
//    private val view: ProfileView,
    private val chatDao: ChatDao,
    private val userId: Int
): BasePresenter<ProfileView>() {

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
                    viewState.showUserProfile(user)
//                    view.showUserProfile(user)
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
                viewState.showRefresh()
//                view.showRefresh()
            }
            .subscribe({ user ->
                viewState.showUserProfile(user)
                viewState.hideRefresh()
//                view.showUserProfile(user)
//                view.hideRefresh()
            }, {
                viewState.showError()
//                view.showError()
            })
            .addTo(compositeDisposable)
    }

    fun getStatus() {
        RetrofitModule.chatApi.getUserPresence(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewState.showStatus(it.presence.userStatus.status)
//                view.showStatus(it.presence.userStatus.status)
            }, {
                viewState.showErrorLoadStatus()
//                view.showErrorLoadStatus()
            })
            .addTo(compositeDisposable)
    }
}