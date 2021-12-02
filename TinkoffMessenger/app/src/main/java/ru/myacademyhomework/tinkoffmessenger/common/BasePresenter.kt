package ru.myacademyhomework.tinkoffmessenger.common

import io.reactivex.disposables.CompositeDisposable
import moxy.MvpPresenter

abstract class BasePresenter<View: BaseView>: MvpPresenter<View>() {
    val compositeDisposable = CompositeDisposable()

    private fun disposeAll() {
        compositeDisposable.clear()
    }

    override fun onDestroy() {
        disposeAll()
        super.onDestroy()
    }
}
