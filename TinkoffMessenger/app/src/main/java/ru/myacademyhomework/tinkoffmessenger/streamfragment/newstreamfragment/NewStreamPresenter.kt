package ru.myacademyhomework.tinkoffmessenger.streamfragment.newstreamfragment

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import ru.myacademyhomework.tinkoffmessenger.common.BasePresenter
import ru.myacademyhomework.tinkoffmessenger.database.ChatDao
import ru.myacademyhomework.tinkoffmessenger.di.ApiClient
import ru.myacademyhomework.tinkoffmessenger.di.newstream.NewStreamScope
import javax.inject.Inject

@NewStreamScope
class NewStreamPresenter @Inject constructor(private val chatDao: ChatDao, private val apiClient: ApiClient)
    : BasePresenter<NewStreamView>(){

        fun createNewStream(nameStream: String, description: String){
            apiClient.chatApi.createStream(
                "[{\"description\": \"$description\", \"name\": \"$nameStream\"}]")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewState.showSuccessCreate()
                },{
                    viewState.showError()
                })
                .addTo(compositeDisposable)
        }
}