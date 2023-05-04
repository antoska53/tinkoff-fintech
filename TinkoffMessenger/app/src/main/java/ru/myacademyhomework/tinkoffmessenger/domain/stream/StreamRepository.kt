package ru.myacademyhomework.tinkoffmessenger.domain.stream

import io.reactivex.Observable

interface StreamRepository {
    fun search(str: String)
    fun initSearch(): Observable<StreamInfo>
}