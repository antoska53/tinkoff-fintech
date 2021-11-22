package ru.myacademyhomework.tinkoffmessenger.chatFragment

import ru.myacademyhomework.tinkoffmessenger.common.BaseView
import ru.myacademyhomework.tinkoffmessenger.network.User
import ru.myacademyhomework.tinkoffmessenger.network.UserMessage

interface ChatView: BaseView {
    fun showRecycler()
    fun showErrorUpdateData()
    fun showErrorSendMessage()
    fun showErrorUpdateEmoji()
    fun hideError()
    fun clearEditText()
    fun initRecycler(listUser: List<User>)
    fun updateRecyclerData(listUseMessage: List<UserMessage>)
    fun addRecyclerData(listUserMessage: List<UserMessage>)
    fun updateMessage(message: UserMessage)
    fun updateMessage(message: UserMessage, position: Int)
    fun addToSharedpref(foundOldest: Boolean)
}