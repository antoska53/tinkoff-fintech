package ru.myacademyhomework.tinkoffmessenger.profilefragment

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.common.BasePresenter
import ru.myacademyhomework.tinkoffmessenger.database.ChatDao
import ru.myacademyhomework.tinkoffmessenger.di.ApiClient
import ru.myacademyhomework.tinkoffmessenger.di.profile.ProfileScope
import ru.myacademyhomework.tinkoffmessenger.network.User
import javax.inject.Inject

@ProfileScope
class ProfilePresenter @Inject constructor(
    private val chatDao: ChatDao,
    private val apiClient: ApiClient
) : BasePresenter<ProfileView>() {

    private var userId: Int = -1

    fun loadUserId(userId: Int){
        this.userId = userId
    }

    fun refreshData() {
        if (userId == ProfileFragment.USER_OWNER) {
            getOwnUser()
        } else {
            getUserFromDb()
            getStatus()
        }
    }

    private fun getUserFromDb() {
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
                }, {

                }
            )
            .addTo(compositeDisposable)
    }

    fun getOwnUser() {
        apiClient.chatApi.getOwnUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                viewState.showRefresh()
            }
            .subscribe({ user ->
                viewState.showUserProfile(user)
                viewState.hideRefresh()
            }, {
                viewState.showError()
            })
            .addTo(compositeDisposable)
    }

    private fun getStatus() {
        apiClient.chatApi.getUserPresence(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                showStatus(it.presence.userStatus.status)
            }, {
                viewState.showErrorLoadStatus()
            })
            .addTo(compositeDisposable)
    }

    private fun showStatus(userStatus: String){
        when (userStatus) {
            "active" -> {
                viewState.showStatus(R.string.status_online, R.color.status_online_color)
            }
            "offline" -> {
                viewState.showStatus(R.string.status_offline, R.color.status_offline_color)
            }
            "idle" -> {
                viewState.showStatus(R.string.status_idle, R.color.status_idle_color)
            }
        }
    }
}