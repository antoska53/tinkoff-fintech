package ru.myacademyhomework.tinkoffmessenger.data.newstream

import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.myacademyhomework.tinkoffmessenger.di.ApiClient
import ru.myacademyhomework.tinkoffmessenger.di.newstream.NewStreamScope
import ru.myacademyhomework.tinkoffmessenger.domain.newstream.NewStreamRepository
import javax.inject.Inject

@NewStreamScope
class NewStreamRepositoryImpl @Inject constructor(
    private val apiClient: ApiClient
) : NewStreamRepository {
    override fun createNewStream(nameStream: String, description: String): Completable {
        return apiClient.chatApi.createStream(
            "[{\"description\": \"$description\", \"name\": \"$nameStream\"}]"
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}