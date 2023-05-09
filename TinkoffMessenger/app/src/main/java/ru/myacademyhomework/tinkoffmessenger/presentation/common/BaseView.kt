package ru.myacademyhomework.tinkoffmessenger.presentation.common

import moxy.MvpView
import moxy.viewstate.strategy.alias.Skip

interface BaseView: MvpView {

    @Skip
    fun showRefresh()

    @Skip
    fun hideRefresh()

    @Skip
    fun showError()
}