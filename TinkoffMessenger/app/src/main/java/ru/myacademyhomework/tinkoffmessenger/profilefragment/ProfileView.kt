package ru.myacademyhomework.tinkoffmessenger.profilefragment

import ru.myacademyhomework.tinkoffmessenger.common.BaseView
import ru.myacademyhomework.tinkoffmessenger.network.User

interface ProfileView: BaseView {
    fun showUserProfile(user: User)
    fun showStatus(userStatus: String)
    fun showErrorLoadStatus()
}