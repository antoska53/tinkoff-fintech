package ru.myacademyhomework.tinkoffmessenger.chatFragment

import moxy.viewstate.strategy.alias.AddToEnd
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.Skip
import ru.myacademyhomework.tinkoffmessenger.common.BaseView
import ru.myacademyhomework.tinkoffmessenger.data.ChatMessage
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

    @AddToEnd
    fun initRecycler(listUser: List<User>)

    @AddToEndSingle
    fun updateRecyclerData(listUserMessage: List<ChatMessage>)

    @AddToEndSingle
    fun addRecyclerData(listUserMessage: List<ChatMessage>)

    @AddToEndSingle
    fun updateMessage(message: UserMessage)

    @AddToEndSingle
    fun updateMessage(message: UserMessage, position: Int)

    @AddToEndSingle
    fun addToSharedpref(foundOldest: Boolean)

    @AddToEndSingle
    fun buttonSendMessageSetImage(resId: Int)

}