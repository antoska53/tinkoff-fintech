package ru.myacademyhomework.tinkoffmessenger.presentation.peoplefragment

import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import ru.myacademyhomework.tinkoffmessenger.common.BasePresenter
import ru.myacademyhomework.tinkoffmessenger.data.database.ChatDao
import ru.myacademyhomework.tinkoffmessenger.data.database.model.UserDb
import ru.myacademyhomework.tinkoffmessenger.di.ApiClient
import ru.myacademyhomework.tinkoffmessenger.di.people.PeopleScope
import ru.myacademyhomework.tinkoffmessenger.domain.people.GetAllUserFromDbUseCase
import ru.myacademyhomework.tinkoffmessenger.domain.people.GetAllUserFromNetworkUseCase
import ru.myacademyhomework.tinkoffmessenger.domain.people.InitSearchUseCase
import ru.myacademyhomework.tinkoffmessenger.domain.people.SearchUseCase
import ru.myacademyhomework.tinkoffmessenger.domain.profile.UserInfo
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@PeopleScope
class PeoplePresenter @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val initSearchUseCase: InitSearchUseCase,
    private val getAllUserFromDbUseCase: GetAllUserFromDbUseCase,
    private val getAllUserFromNetworkUseCase: GetAllUserFromNetworkUseCase
) :
    BasePresenter<PeopleView>() {

    private var databaseIsEmpty = true
    private var isSearch = false
    fun search(str: String) {
        if (str.isEmpty() && isSearch) {
            viewState.showUsers()
            isSearch = false
        }
        else searchUseCase.search(str)
    }

    fun initSearch() {
        initSearchUseCase.initSearch()
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
        getAllUsersFromNetwork()
    }

    fun getAllUsersFromDb() {
        getAllUserFromDbUseCase.getAllUser()
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

    fun getAllUsersFromNetwork() {
        getAllUserFromNetworkUseCase.getAllUser()
            .doOnTerminate {
                viewState.hideRefresh()
            }
            .subscribe({
            },{
                if(databaseIsEmpty){
                    viewState.showError()
                }
            })
            .addTo(compositeDisposable)
    }
}