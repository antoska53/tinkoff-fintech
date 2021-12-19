package ru.myacademyhomework.tinkoffmessenger.chatFragment

import moxy.viewstate.strategy.alias.AddToEnd
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution
import moxy.viewstate.strategy.alias.Skip
import ru.myacademyhomework.tinkoffmessenger.common.BaseView
import ru.myacademyhomework.tinkoffmessenger.data.ChatMessage
import ru.myacademyhomework.tinkoffmessenger.database.TopicDb
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
    fun updateRecyclerData(listUserMessage: List<ChatMessage>)

//    @AddToEndSingle
    @Skip
    fun addRecyclerData(listUserMessage: List<ChatMessage>)

    @AddToEndSingle
    fun updateMessage(message: UserMessage, isStreamChat: Boolean)

    @Skip
    fun updateMessage(message: UserMessage, position: Int)

    @Skip
    fun copyMessage(message: String)

    @Skip
    fun addToSharedpref(foundOldest: Boolean)

    @AddToEndSingle
    fun buttonSendMessageSetImage(resId: Int)

    @Skip
    fun showErrorAddReaction()

    @Skip
    fun showErrorRemoveReaction()

    @Skip
    fun openChatTopic(nameStream: String, nameTopic: String)

    @Skip
    fun showPopupMenu(listTopic: List<TopicDb>)

    @Skip
    fun showErrorPopupMenu()

    @Skip
    fun showErrorChooseTopic()

    @Skip
    fun showErrorDeleteMessage()

    @Skip
    fun showDeleteMessageSuccess()

    @Skip
    fun showErrorEmptyEditMessage()

    @Skip
    fun showErrorEditMessage()

    @Skip
    fun setupEditMessage(messageId: Long, nameTopic: String, message: String)

    @Skip
    fun destroyEditMessage()

    @OneExecution
    fun showNameTopic(isVisible: Int)

    @OneExecution
    fun showChooseTopic(isVisible: Int)

    @Skip
    fun showEmojiBottomSheetDialog(idMessage: Long, nameTopic: String, positionMessage: Int)
}