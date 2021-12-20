package ru.myacademyhomework.tinkoffmessenger.profilefragment

import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution
import moxy.viewstate.strategy.alias.Skip
import ru.myacademyhomework.tinkoffmessenger.common.BaseView
import ru.myacademyhomework.tinkoffmessenger.network.User

interface ProfileView : BaseView {

    @AddToEndSingle
    fun showUserProfile(user: User)

    @AddToEndSingle
    fun showStatus(idStatus: Int, idColor: Int)

    @Skip
    fun showErrorLoadStatus()

    @OneExecution
    override fun showRefresh()
}