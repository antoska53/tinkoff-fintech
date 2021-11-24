package ru.myacademyhomework.tinkoffmessenger.common

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface BaseView: MvpView {

    @AddToEndSingle
    fun showRefresh()

    @AddToEndSingle
    fun hideRefresh()

    @AddToEndSingle
    fun showError()
}