package ru.myacademyhomework.tinkoffmessenger.presentation.chatfragment

import android.text.Editable
import android.text.Html
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxkotlin.addTo
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.common.BasePresenter
import ru.myacademyhomework.tinkoffmessenger.data.*
import ru.myacademyhomework.tinkoffmessenger.di.chat.ChatScope
import ru.myacademyhomework.tinkoffmessenger.domain.chat.AddReactionUseCase
import ru.myacademyhomework.tinkoffmessenger.domain.chat.DeleteMessageUseCase
import ru.myacademyhomework.tinkoffmessenger.domain.chat.EditMessageUseCase
import ru.myacademyhomework.tinkoffmessenger.domain.chat.GetAllMessagesFromDbForStreamUseCase
import ru.myacademyhomework.tinkoffmessenger.domain.chat.GetAllMessagesFromDbUseCase
import ru.myacademyhomework.tinkoffmessenger.domain.chat.GetMessageUseCase
import ru.myacademyhomework.tinkoffmessenger.domain.chat.GetMessagesForStreamUseCase
import ru.myacademyhomework.tinkoffmessenger.domain.chat.GetMessagesUseCase
import ru.myacademyhomework.tinkoffmessenger.domain.chat.GetOldMessageFromDbUseCase
import ru.myacademyhomework.tinkoffmessenger.domain.chat.GetOwnUserUseCase
import ru.myacademyhomework.tinkoffmessenger.domain.chat.InitChatUseCase
import ru.myacademyhomework.tinkoffmessenger.domain.chat.RemoveReactionUseCase
import ru.myacademyhomework.tinkoffmessenger.domain.chat.SendMessageUseCase
import ru.myacademyhomework.tinkoffmessenger.domain.chat.SetupListMessageForStreamUseCase
import ru.myacademyhomework.tinkoffmessenger.domain.chat.SetupListMessageUseCase
import ru.myacademyhomework.tinkoffmessenger.domain.chat.ShowPopupMenuUseCase
import ru.myacademyhomework.tinkoffmessenger.domain.chat.UpdateEmojiUseCase
import javax.inject.Inject


@ChatScope
class ChatPresenter @Inject constructor(
    private val addReactionUseCase: AddReactionUseCase,
    private val deleteMessageUseCase: DeleteMessageUseCase,
    private val editMessageUseCase: EditMessageUseCase,
    private val getAllMessagesFromDbForStreamUseCase: GetAllMessagesFromDbForStreamUseCase,
    private val getAllMessagesFromDbUseCase: GetAllMessagesFromDbUseCase,
    private val getMessagesForStreamUseCase: GetMessagesForStreamUseCase,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val getMessageUseCase: GetMessageUseCase,
    private val getOldMessageFromDbUseCase: GetOldMessageFromDbUseCase,
    private val getOwnUserUseCase: GetOwnUserUseCase,
    private val initChatUseCase: InitChatUseCase,
    private val removeReactionUseCase: RemoveReactionUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val setupListMessageForStreamUseCase: SetupListMessageForStreamUseCase,
    private val setupListMessageUseCase: SetupListMessageUseCase,
    private val showPopupMenuUseCase: ShowPopupMenuUseCase,
    private val updateEmojiUseCase: UpdateEmojiUseCase
) : BasePresenter<ChatView>() {

    private var databaseIsRefresh = false
    private var databaseIsNotEmpty = false
    private var isLoading = true
    private var firstMessageId = "-1"
    private var nameStream: String? = null
    private var nameTopic: String? = null
    private var isInitRecycler = false
    private var foundOldest = false

    fun load(nameStream: String, nameTopic: String, foundOldest: Boolean) {
        this.nameStream = nameStream
        this.nameTopic = nameTopic
        this.foundOldest = foundOldest
    }

    fun buttonReloadClick() {
        if (isInitRecycler) {
            getMessages()
        } else {
            initChat()
        }
    }

    fun showPopupMenu() {
        nameStream?.let { nameStream ->
        showPopupMenuUseCase.showPopupMenu(nameStream)
                .subscribe({
                    viewState.showPopupMenu(it)
                }, {
                    viewState.showErrorPopupMenu()
                })
                .addTo(compositeDisposable)
        }
    }

    fun showChooseTopic(){
        if(nameTopic == ChatFragment.STREAM_CHAT){
            viewState.showChooseTopic(View.VISIBLE)
        }else{
            viewState.showChooseTopic(View.GONE)
        }
    }

    fun showNameTopic(){
        if(nameTopic == ChatFragment.STREAM_CHAT){
            viewState.showNameTopic(View.GONE)
        }else{
            viewState.showNameTopic(View.VISIBLE)
        }
    }

    fun loadFoundOldest(foundOldest: Boolean) {
        this.foundOldest = foundOldest
    }

    fun buttonSendMessageSetImage(str: String) {
        if (str.isNotEmpty()) viewState.buttonSendMessageSetImage(R.drawable.ic_plane)
        else viewState.buttonSendMessageSetImage(R.drawable.ic_cross)
    }

    fun onClickButtonSendMessage(text: Editable, nameTopic: String) {
        this.nameTopic?.let {
            if (it != ChatFragment.STREAM_CHAT) {
                sendMessage(text.toString(), it)
            } else if (nameTopic == ChatFragment.CHOOSE_TOPIC) {
                viewState.showErrorChooseTopic()
            } else if (text.isNotEmpty()) {
                sendMessage(message = text.toString(), nameTopic)
            }
        }

    }

    fun onClickButtonEditMessage(messageId: Long, text: Editable, nameTopic: String){
        if(text.toString().isNotEmpty() && nameTopic != ChatFragment.CHOOSE_TOPIC){
            editMessage(messageId, nameTopic, text.toString())
        }else{
            viewState.showErrorEmptyEditMessage()
        }
    }

    fun onClickTopic(nameTopic: String) {
        nameStream?.let {
            viewState.openChatTopic(it, nameTopic)
        }
    }

    fun initChat() {
        initChatUseCase.initChat()
            .subscribe({
                if (it.isEmpty()) {
                    Log.d("OWN", "initChat: isEmpty $it")
                    getOwnUser()
                } else {
                    viewState.hideError()
                    viewState.initRecycler(it)
                    isInitRecycler = true
                    databaseIsRefresh = false
                    if (nameTopic == ChatFragment.STREAM_CHAT) getAllMessagesFromDbForStream()
                    else getAllMessagesFromDb()
                }
            }, {
            })
            .addTo(compositeDisposable)
    }

    fun pagingChat(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
        if (!isLoading) {
            if (!foundOldest) {
                if (firstVisibleItem - ChatFragment.POSITION_FOR_LOAD <= ChatFragment.FIRST_POSITION) {
                    isLoading = true
                    compositeDisposable.clear()
                    getMessages(firstMessageId)
                }
            }
        }
    }

    private fun getOwnUser() {
        getOwnUserUseCase.getOwnUser()
            .subscribe({
                viewState.hideError()
            }, {
                viewState.showError()
            })
            .addTo(compositeDisposable)
    }

    private fun getOldMessageFromDb() {
        nameTopic?.let { nameTopic ->
            getOldMessageFromDbUseCase.getOldMessageFromDb(nameTopic, firstMessageId.toLong())
                .subscribe({
                    if(it.isNotEmpty()) firstMessageId = it.first().id.toString()
                    viewState.addRecyclerData(setupListMessage(it))
                    isLoading = false
                },{})
                .addTo(compositeDisposable)
        }
    }

    private fun getAllMessagesFromDb() {
        nameTopic?.let { nameTopic ->
            nameStream?.let { nameStream ->
                getAllMessagesFromDbUseCase.getAllMessagesFromDb(nameTopic, nameStream)
                    .subscribe({
                        if (it.isEmpty()) {
                            viewState.showRefresh()
                        } else {
                            if(it.isNotEmpty()) firstMessageId = it.first().id.toString()
                            databaseIsNotEmpty = true
                            viewState.updateRecyclerData(setupListMessage(it))
                            isLoading = false
                        }
                        if (!databaseIsRefresh){
                            getMessages("newest")}
                    }, {
                    })
                    .addTo(compositeDisposable)
            }
        }
    }

    private fun getAllMessagesFromDbForStream() {
        nameStream?.let { nameStream ->
            getAllMessagesFromDbForStreamUseCase.getAllMessagesFromDbForStream(nameStream)
                .subscribe({
                    if (it.isEmpty()) {
                        viewState.showRefresh()
                        isLoading = false
                    } else {
                        databaseIsNotEmpty = true
                        viewState.updateRecyclerData(setupListMessageForStream(it))
                        isLoading = false
                    }
                    if (!databaseIsRefresh) getMessagesForStream("newest")
                }, {
                })
                .addTo(compositeDisposable)
        }
    }

    private fun getMessages() {
        getMessages(firstMessageId)
    }

    private fun getMessages(anchor: String) {
        nameTopic?.let { nameTopic ->
            nameStream?.let { nameStream ->
                getMessagesUseCase.getMessages(anchor, nameStream, nameTopic)
                    .doOnSuccess {
                        if (it.foundOldest) {
                            foundOldest = it.foundOldest
                            viewState.addToSharedpref(foundOldest)
                        }
                        databaseIsRefresh = true
                        databaseIsNotEmpty = true
                        if (isLoading) getOldMessageFromDb()
                    }
                    .doOnSubscribe {
                        if (!databaseIsNotEmpty) {
                            viewState.showRefresh()
                        }
                    }
                    .doOnTerminate {
                        viewState.hideRefresh()
                    }
                    .subscribe({
                        viewState.showRecycler()
                    }, {
                        if (databaseIsNotEmpty) {
                            viewState.showErrorUpdateData()
                        } else {
                            viewState.showError()
                        }
                        isLoading = false
                    })
                    .addTo(compositeDisposable)
            }
        }
    }

    private fun getMessagesForStream(anchor: String) {
        nameStream?.let { nameStream ->
            getMessagesForStreamUseCase.getMessagesForStream(anchor, nameStream)
                .doOnSuccess {
                    databaseIsRefresh = true
                    databaseIsNotEmpty = true
                }
                .doOnSubscribe {
                    if (!databaseIsNotEmpty) {
                        viewState.showRefresh()
                    }
                }
                .doOnTerminate {
                    viewState.hideRefresh()
                }
                .subscribe({
                    viewState.showRecycler()
                }, {
                    if (databaseIsNotEmpty) {
                        viewState.showErrorUpdateData()
                    } else {
                        viewState.showError()
                    }
                    isLoading = false
                })
                .addTo(compositeDisposable)
        }
    }

    private fun getMessage(messageId: Long, position: Int, nameTopic: String?) {
        nameTopic?.let {
            if (it != ChatFragment.STREAM_CHAT) {
                getMessageUseCase.getMessage(messageId, position, nameTopic, nameStream)
                    .subscribe({
                        if (position == ChatFragment.SEND_MESSAGE_POSITION) {
                            val message = it.messages[0]
                            message.nameTopic = nameTopic
                        }
                    }, {
                    })
                    .addTo(compositeDisposable)
            }
        }
    }

    private fun sendMessage(message: String, nameTopic: String) {
        nameStream?.let { nameStream ->
            sendMessageUseCase.sendMessage(message, nameTopic, nameStream)
                .subscribe(
                    {
                        viewState.clearEditText()
                        getMessage(it.id, ChatFragment.SEND_MESSAGE_POSITION, nameTopic)
                    },
                    {
                        viewState.showErrorSendMessage()
                    }
                )
                .addTo(compositeDisposable)
        }
    }

    fun updateEmoji(emoji: String, idMessage: Long, nameTopic: String, position: Int) {
        updateEmojiUseCase.updateEmoji(emoji, idMessage, nameTopic, position)
            .subscribe({
                getMessage(idMessage, position, nameTopic)
            },
                {
                    viewState.showErrorUpdateEmoji()
                })
            .addTo(compositeDisposable)
    }

    private fun setupListMessage(messages: List<UserMessage>): List<ChatMessage> {
        return setupListMessageUseCase.setupListMessage(messages)
    }

    private fun setupListMessageForStream(messages: List<UserMessage>): List<ChatMessage> {
        return setupListMessageForStreamUseCase.setupListMessageForStream(messages)
    }

    fun addReaction(messageId: Long, nameTopic: String, emojiName: String, position: Int) {
        addReactionUseCase.addReaction(messageId, nameTopic, emojiName, position)
            .subscribe({
                getMessage(messageId, position, nameTopic)
            },
                {
                    viewState.showErrorAddReaction()
                })
            .addTo(compositeDisposable)
    }

    fun removeReaction(messageId: Long, nameTopic: String, emojiName: String, emojiCode: String,
        reactionType: String, userId: Int, position: Int
    ) {
        removeReactionUseCase.removeReaction(messageId,  emojiName, emojiCode, reactionType, userId)
            .subscribe({
                getMessage(messageId, position, nameTopic)
            }, {
                viewState.showErrorRemoveReaction()
            })
            .addTo(compositeDisposable)
    }

   private fun deleteMessage(idMessage: Long){
       deleteMessageUseCase.deleteMessage(idMessage)
            .subscribe({
                viewState.showDeleteMessageSuccess()
            },{
                viewState.showErrorDeleteMessage()
            })
            .addTo(compositeDisposable)
   }

    private fun editMessage(messageId: Long, nameTopic: String, message: String ){
        editMessageUseCase.editMessage(messageId, nameTopic, message)
            .subscribe({
                viewState.destroyEditMessage()
            },{
                viewState.showErrorEditMessage()
            })
            .addTo(compositeDisposable)
    }

    fun actionBottomSheet(action: ActionBottomSheet, idMessage: Long, nameTopic: String, messageForEdit: String, positionMessage: Int){
        when(action){
            ActionBottomSheet.ADD_REACTION -> {
                viewState.showEmojiBottomSheetDialog(idMessage, nameTopic, positionMessage)
            }
            ActionBottomSheet.DELETE_MESSAGE -> {
                deleteMessage(idMessage)
            }
            ActionBottomSheet.EDIT_MESSAGE -> {
                val message = Html.fromHtml(messageForEdit, Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH).trim().toString()
                viewState.setupEditMessage(idMessage, nameTopic, message)
            }
            ActionBottomSheet.COPY_MESSAGE -> {
                val message = Html.fromHtml(messageForEdit, Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH).trim().toString()
                viewState.copyMessage(message)

            }
        }
    }
}

