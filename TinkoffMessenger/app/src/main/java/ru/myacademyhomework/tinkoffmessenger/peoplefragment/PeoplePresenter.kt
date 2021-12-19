package ru.myacademyhomework.tinkoffmessenger.peoplefragment

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
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

    private var databaseIsEmpty = true

    fun openProfile(userId: Int) {
        viewState.openProfileFragment(userId)
    }

    fun buttonReloadClick(){
        getAllUsersFromDb()
        getAllUsers()
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
                if(it.isNotEmpty()){
                    databaseIsEmpty = false
                    viewState.hideRefresh()
                    viewState.updateRecycler(it)
                }else{
                    viewState.showRefresh()
                }
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
            .doOnTerminate {
                viewState.hideRefresh()
            }
            .subscribe({
//                viewState.hideRefresh()
            },{
//                viewState.hideRefresh()
                if(databaseIsEmpty){
                    viewState.showError()
                }else{
                    viewState.showErrorUpdateData()
                }
            })
            .addTo(compositeDisposable)
    }
}