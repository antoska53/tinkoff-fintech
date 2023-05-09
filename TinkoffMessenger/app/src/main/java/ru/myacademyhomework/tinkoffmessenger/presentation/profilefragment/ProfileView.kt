package ru.myacademyhomework.tinkoffmessenger.presentation.profilefragment

import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution
import moxy.viewstate.strategy.alias.Skip
import ru.myacademyhomework.tinkoffmessenger.presentation.common.BaseView
import ru.myacademyhomework.tinkoffmessenger.domain.profile.UserInfo

interface ProfileView : BaseView {

    @AddToEndSingle
    fun showUserProfile(user: UserInfo)

    @AddToEndSingle
    fun showStatus(idStatus: Int, idColor: Int)

    @Skip
    fun showErrorLoadStatus()

    @OneExecution
    override fun showRefresh()
}