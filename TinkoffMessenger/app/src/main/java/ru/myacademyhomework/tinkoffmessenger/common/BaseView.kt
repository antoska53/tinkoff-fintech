package ru.myacademyhomework.tinkoffmessenger.common

import moxy.MvpView

interface BaseView: MvpView {

    fun showRefresh()

    fun hideRefresh()

    fun showError()
}