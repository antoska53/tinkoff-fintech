package ru.myacademyhomework.tinkoffmessenger.peoplefragment

import moxy.viewstate.strategy.alias.AddToEndSingle
import ru.myacademyhomework.tinkoffmessenger.common.BaseView
import ru.myacademyhomework.tinkoffmessenger.network.User

interface PeopleView : BaseView {

    @AddToEndSingle
    fun updateRecycler(users: List<User>)

    @AddToEndSingle
    fun openProfileFragment(userId: Int)
}