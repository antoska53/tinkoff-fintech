package ru.myacademyhomework.tinkoffmessenger.domain.newstream

import io.reactivex.Completable

interface NewStreamRepository {
    fun createNewStream(nameStream: String, description: String): Completable
}