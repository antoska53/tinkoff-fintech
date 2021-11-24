package ru.myacademyhomework.tinkoffmessenger.chatFragment

import moxy.viewstate.strategy.alias.AddToEndSingle
import ru.myacademyhomework.tinkoffmessenger.common.BaseView
import ru.myacademyhomework.tinkoffmessenger.network.User
import ru.myacademyhomework.tinkoffmessenger.network.UserMessage

interface ChatView : BaseView {

    @AddToEndSingle
    fun showRecycler()

    @AddToEndSingle
    fun showErrorUpdateData()

    @AddToEndSingle
    fun showErrorSendMessage()

    @AddToEndSingle
    fun showErrorUpdateEmoji()

    @AddToEndSingle
    fun hideError()

    @AddToEndSingle
    fun clearEditText()

    @AddToEndSingle
    fun initRecycler(listUser: List<User>)

    @AddToEndSingle
    fun updateRecyclerData(listUseMessage: List<UserMessage>)

    @AddToEndSingle
    fun addRecyclerData(listUserMessage: List<UserMessage>)

    @AddToEndSingle
    fun updateMessage(message: UserMessage)

    @AddToEndSingle
    fun updateMessage(message: UserMessage, position: Int)

    @AddToEndSingle
    fun addToSharedpref(foundOldest: Boolean)
}