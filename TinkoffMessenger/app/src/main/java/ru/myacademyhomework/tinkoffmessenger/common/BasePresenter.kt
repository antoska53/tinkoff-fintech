package ru.myacademyhomework.tinkoffmessenger.common

import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter {
    val compositeDisposable = CompositeDisposable()

    fun disposeAll() {
        compositeDisposable.clear()
    }
}
