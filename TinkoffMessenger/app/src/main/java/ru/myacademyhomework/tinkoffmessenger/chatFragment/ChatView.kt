package ru.myacademyhomework.tinkoffmessenger.chatFragment

import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.Skip
import ru.myacademyhomework.tinkoffmessenger.common.BaseView
import ru.myacademyhomework.tinkoffmessenger.network.User
import ru.myacademyhomework.tinkoffmessenger.network.UserMessage

interface ChatView : BaseView {

    @Skip
    fun showRecycler()

    @Skip
    fun showErrorUpdateData()

    @Skip
    fun showErrorSendMessage()

    @Skip
    fun showErrorUpdateEmoji()

    @Skip
    fun hideError()

    @Skip
    fun clearEditText()

    @AddToEndSingle
    fun initRecycler(listUser: List<User>)

    @AddToEndSingle
    fun updateRecyclerData(listUserMessage: List<UserMessage>)

    @AddToEndSingle
    fun addRecyclerData(listUserMessage: List<UserMessage>)

    @AddToEndSingle
    fun updateMessage(message: UserMessage)

    @AddToEndSingle
    fun updateMessage(message: UserMessage, position: Int)

    @AddToEndSingle
    fun addToSharedpref(foundOldest: Boolean)

    @AddToEndSingle
    fun buttonSendMessageSetImage(resId: Int)

}