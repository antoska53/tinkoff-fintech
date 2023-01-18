package ru.myacademyhomework.tinkoffmessenger.peoplefragment

import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.Skip
import ru.myacademyhomework.tinkoffmessenger.common.BaseView
import ru.myacademyhomework.tinkoffmessenger.network.User

interface PeopleView : BaseView {

    @AddToEndSingle
    fun updateRecycler(users: List<User>)

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