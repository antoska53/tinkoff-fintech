package ru.myacademyhomework.tinkoffmessenger.peoplefragment

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import ru.myacademyhomework.tinkoffmessenger.common.BasePresenter
import ru.myacademyhomework.tinkoffmessenger.database.ChatDao
import ru.myacademyhomework.tinkoffmessenger.database.UserDb
import ru.myacademyhomework.tinkoffmessenger.di.ApiClient
import ru.myacademyhomework.tinkoffmessenger.di.people.PeopleScope
import ru.myacademyhomework.tinkoffmessenger.network.User
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@PeopleScope
class PeoplePresenter @Inject constructor(
    private val chatDao: ChatDao,
    private val apiClient: ApiClient
) :
    BasePresenter<PeopleView>() {

    private var databaseIsEmpty = true
    private var isSearch = false
    private val subject = PublishSubject.create<String>()

    fun search(str: String) {
        if (str.isEmpty() && isSearch) {
            viewState.showUsers()
            isSearch = false
        }
        else subject.onNext(str)
    }

    fun initSearch() {
        subject
            .filter { str -> str.isNotEmpty() }
            .distinctUntilChanged()
            .debounce(1000, TimeUnit.MILLISECONDS)
            .switchMap { str ->
                val userDb = chatDao.getUserForSearch(str)
                if (userDb != null) {
                    Observable.just(
                        User(
                            avatarURL = userDb.avatarURL,
                            email = userDb.email,
                            fullName = userDb.fullName,
                            userID = userDb.userID
                        )
                    )
                }
                else {
                    Observable.just(User("", "", "", 0))
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { user ->
                    if (user.fullName.isNotEmpty()) {
                        isSearch = true
                        viewState.updateRecycler(listOf(user))
                    } else {
                        viewState.showIsEmptyResultSearch()
                    }
                },
                {
                    viewState.showError()
                }
            )
            .addTo(compositeDisposable)
    }


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
            },{
                if(databaseIsEmpty){
                    viewState.showError()
                }else{
                    viewState.showErrorUpdateData()
                }
            })
            .addTo(compositeDisposable)
    }
}