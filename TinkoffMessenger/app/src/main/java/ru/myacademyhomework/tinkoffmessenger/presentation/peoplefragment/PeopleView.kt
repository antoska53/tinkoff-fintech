package ru.myacademyhomework.tinkoffmessenger.presentation.peoplefragment

import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.Skip
import ru.myacademyhomework.tinkoffmessenger.presentation.common.BaseView
import ru.myacademyhomework.tinkoffmessenger.domain.profile.UserInfo

interface PeopleView : BaseView {

    @AddToEndSingle
    fun updateRecycler(users: List<UserInfo>)

    @Skip
    fun openProfileFragment(userId: Int)

    @Skip
    fun showRecycler()

    @Skip
    fun showErrorUpdateData()

    @Skip
    fun showIsEmptyResultSearch()

    @Skip
    fun showUsers()
}