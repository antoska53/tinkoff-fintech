package ru.myacademyhomework.tinkoffmessenger.profilefragment

import moxy.viewstate.strategy.alias.AddToEndSingle
import ru.myacademyhomework.tinkoffmessenger.common.BaseView
import ru.myacademyhomework.tinkoffmessenger.network.User

interface ProfileView: BaseView {

    @AddToEndSingle
    fun showUserProfile(user: User)

    @AddToEndSingle
    fun showStatus(userStatus: String)

    @AddToEndSingle
    fun showErrorLoadStatus()
}