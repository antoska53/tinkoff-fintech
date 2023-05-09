package ru.myacademyhomework.tinkoffmessenger.presentation.profilefragment

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.presentation.common.BasePresenter
import ru.myacademyhomework.tinkoffmessenger.di.profile.ProfileScope
import ru.myacademyhomework.tinkoffmessenger.domain.profile.GetStatusUseCase
import ru.myacademyhomework.tinkoffmessenger.domain.profile.GetUserInfoUseCase
import javax.inject.Inject

@ProfileScope
class ProfilePresenter @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getStatusUseCase: GetStatusUseCase
) : BasePresenter<ProfileView>() {

    private var userId: Int = -1

    fun loadUserId(userId: Int){
        this.userId = userId
    }

    fun refreshData() {
        getUserInfoUseCase.getUserInfo(userId)
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

        getStatusUseCase.getStatus(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ userStatus ->
                showStatus(userStatus.status)
            }, {
            })
            .addTo(compositeDisposable)
    }

    private fun showStatus(userStatus: String){
        when (userStatus) {
            ProfileFragment.STATUS_ACTIVE -> {
                viewState.showStatus(R.string.status_online, R.color.status_online_color)
            }
            ProfileFragment.STATUS_OFFLINE -> {
                viewState.showStatus(R.string.status_offline, R.color.status_offline_color)
            }
            ProfileFragment.STATUS_IDLE -> {
                viewState.showStatus(R.string.status_idle, R.color.status_idle_color)
            }
        }
    }
}