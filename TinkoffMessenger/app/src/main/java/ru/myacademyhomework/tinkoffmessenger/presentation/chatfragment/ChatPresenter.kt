package ru.myacademyhomework.tinkoffmessenger.presentation.chatfragment

import android.text.Editable
import android.text.Html
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.common.BasePresenter
import ru.myacademyhomework.tinkoffmessenger.data.*
import ru.myacademyhomework.tinkoffmessenger.data.database.ChatDao
import ru.myacademyhomework.tinkoffmessenger.data.database.model.MessageDb
import ru.myacademyhomework.tinkoffmessenger.data.database.model.ReactionDb
import ru.myacademyhomework.tinkoffmessenger.data.database.model.UserDb
import ru.myacademyhomework.tinkoffmessenger.data.network.model.Reaction
import ru.myacademyhomework.tinkoffmessenger.data.network.model.UserDto
import ru.myacademyhomework.tinkoffmessenger.data.network.model.UserMessage
import ru.myacademyhomework.tinkoffmessenger.di.ApiClient
import ru.myacademyhomework.tinkoffmessenger.di.chat.ChatScope
import java.time.Instant
import java.time.ZoneId
import java.util.*
import javax.inject.Inject


@ChatScope
class ChatPresenter @Inject constructor(
    private val chatDao: ChatDao,
    private val apiClient: ApiClient
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
            chatDao.getTopicsForStream(nameStream)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
        chatDao
            .getOwnUser()
            .map {
                it.map { userDb ->
                    UserDto(
                        avatarURL = userDb.avatarURL,
                        email = userDb.email,
                        fullName = userDb.fullName,
                        userID = userDb.userID
                    )
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isEmpty()) {
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
        apiClient.chatApi.getOwnUser()
            .subscribeOn(Schedulers.io())
            .map {
                UserDb(
                    avatarURL = it.avatarURL,
                    email = it.email,
                    fullName = it.fullName,
                    userID = it.userID,
                    isOwn = true
                )
            }
            .doOnSuccess {
                chatDao.insertOwnUser(it)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewState.hideError()
            }, {
                viewState.showError()
            })
            .addTo(compositeDisposable)
    }

    private fun getOldMessageFromDb() {
        nameTopic?.let { nameTopic ->
            chatDao.getOldMessages(nameTopic, firstMessageId.toLong())
                .map {
                    it.map { messageDb ->
                        UserMessage(
                            avatarURL = messageDb.avatarURL,
                            content = messageDb.content,
                            id = messageDb.id,
                            isMeMessage = messageDb.isMeMessage,
                            senderFullName = messageDb.senderFullName,
                            timestamp = messageDb.timestamp,
                            streamID = messageDb.streamID,
                            reactions = chatDao.getReaction(messageDb.id).map { reactionDb ->
                                Reaction(
                                    reactionDb.emojiCode,
                                    reactionDb.emojiName,
                                    reactionDb.reactionType,
                                    reactionDb.userId
                                )
                            }
                        )
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
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
                chatDao.getAllMessages(nameTopic, nameStream)
                    .map {
                        it.map { messageDb ->
                            UserMessage(
                                avatarURL = messageDb.avatarURL,
                                content = messageDb.content,
                                id = messageDb.id,
                                isMeMessage = messageDb.isMeMessage,
                                senderFullName = messageDb.senderFullName,
                                timestamp = messageDb.timestamp,
                                streamID = messageDb.streamID,
                                nameTopic = nameTopic,
                                reactions = chatDao.getReaction(messageDb.id).map { reactionDb ->
                                    Reaction(
                                        reactionDb.emojiCode,
                                        reactionDb.emojiName,
                                        reactionDb.reactionType,
                                        reactionDb.userId
                                    )
                                }
                            )
                        }
                    }
                    .observeOn(AndroidSchedulers.mainThread())
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
            chatDao.getAllMessagesForStream(nameStream)
                .map {
                    it.map { messageDb ->
                        UserMessage(
                            avatarURL = messageDb.avatarURL,
                            content = messageDb.content,
                            id = messageDb.id,
                            isMeMessage = messageDb.isMeMessage,
                            senderFullName = messageDb.senderFullName,
                            timestamp = messageDb.timestamp,
                            streamID = messageDb.streamID,
                            nameTopic = messageDb.nameTopic,
                            reactions = chatDao.getReaction(messageDb.id).map { reactionDb ->
                                Reaction(
                                    reactionDb.emojiCode,
                                    reactionDb.emojiName,
                                    reactionDb.reactionType,
                                    reactionDb.userId
                                )
                            }
                        )
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
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
                apiClient.chatApi.getMessages(
                    anchor = anchor,
                    num_after = 0,
                    num_before = 20,
                    narrow = "[{\"operand\":\"$nameStream\", \"operator\":\"stream\"},{\"operand\":\"$nameTopic\",\"operator\":\"topic\"}]"
                )
                    .subscribeOn(Schedulers.io())
                    .map {
                        if (it.foundOldest) {
                            foundOldest = it.foundOldest
                            viewState.addToSharedpref(foundOldest)
                        }
                        it.messages.map { userMessage ->
                            chatDao.insertReaction(
                                userMessage.reactions.map { reaction ->
                                    ReactionDb(
                                        reaction.emojiCode,
                                        reaction.emojiName,
                                        reaction.reactionType,
                                        reaction.userId,
                                        userMessage.id
                                    )
                                })

                            MessageDb(
                                avatarURL = userMessage.avatarURL,
                                content = userMessage.content,
                                id = userMessage.id,
                                isMeMessage = userMessage.isMeMessage,
                                senderFullName = userMessage.senderFullName,
                                timestamp = userMessage.timestamp,
                                streamID = userMessage.streamID,
                                nameTopic = nameTopic,
                                nameStream = nameStream
                            )
                        }
                    }
                    .doOnSuccess {
                        databaseIsRefresh = true
                        databaseIsNotEmpty = true
                        chatDao.insertMessages(it)
                        if (isLoading) getOldMessageFromDb()
                    }
                    .observeOn(AndroidSchedulers.mainThread())
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
            chatDao.getTopicsForStream(nameStream)
                .subscribeOn(Schedulers.io())
                .flatMapObservable {
                    Observable.fromIterable(it)
                }
                .flatMapSingle { topicDb ->
                    apiClient.chatApi.getMessages(
                        anchor = anchor,
                        num_after = 0,
                        num_before = 5,
                        narrow = "[{\"operand\":\"$nameStream\", \"operator\":\"stream\"},{\"operand\":\"${topicDb.nameTopic}\",\"operator\":\"topic\"}]"
                    )
                        .map { messageResponse ->
                            messageResponse.messages.map { userMessage ->
                                chatDao.insertReaction(
                                    userMessage.reactions.map { reaction ->
                                        ReactionDb(
                                            reaction.emojiCode,
                                            reaction.emojiName,
                                            reaction.reactionType,
                                            reaction.userId,
                                            userMessage.id
                                        )
                                    })

                                MessageDb(
                                    avatarURL = userMessage.avatarURL,
                                    content = userMessage.content,
                                    id = userMessage.id,
                                    isMeMessage = userMessage.isMeMessage,
                                    senderFullName = userMessage.senderFullName,
                                    timestamp = userMessage.timestamp,
                                    streamID = userMessage.streamID,
                                    nameTopic = topicDb.nameTopic,
                                    nameStream = nameStream
                                )
                            }
                        }
                }
                .toList()
                .doOnSuccess {
                    databaseIsRefresh = true
                    databaseIsNotEmpty = true
                    chatDao.insertMessages(it.flatten())
                }
                .observeOn(AndroidSchedulers.mainThread())
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
                apiClient.chatApi.getMessages(
                    "newest",
                    1,
                    1,
                    "[{\"operand\":\"$nameStream\", \"operator\":\"stream\"},{\"operand\":\"$nameTopic\",\"operator\":\"topic\"},{\"operand\":\"$messageId\",\"operator\":\"id\"}]"
                )
                    .doOnSuccess {
                        it.messages[0].nameTopic = nameTopic
                        val message = it.messages[0]
                        chatDao.insertReaction(
                            message.reactions.map { reaction ->
                                ReactionDb(
                                    reaction.emojiCode,
                                    reaction.emojiName,
                                    reaction.reactionType,
                                    reaction.userId,
                                    message.id
                                )
                            })
                        chatDao.insertMessage(
                            MessageDb(
                                avatarURL = message.avatarURL,
                                content = message.content,
                                id = message.id,
                                isMeMessage = message.isMeMessage,
                                senderFullName = message.senderFullName,
                                timestamp = message.timestamp,
                                streamID = message.streamID,
                                nameTopic = message.nameTopic,
                                nameStream = nameStream!!
                        )
                        )
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
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
            apiClient.chatApi.sendMessage("stream", nameStream, nameTopic, message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
        val emojiName = Emoji.values().find {
            it.unicode == emoji
        }

        apiClient.chatApi.addReaction(idMessage, emojiName?.nameInZulip ?: "")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                getMessage(idMessage, position, nameTopic)
            },
                {
                    viewState.showErrorUpdateEmoji()
                })
            .addTo(compositeDisposable)
    }

    private fun setupListMessage(messages: List<UserMessage>): List<ChatMessage> {
        val newListMessages = mutableListOf<ChatMessage>()
        var calendar: Calendar? = null
        for (message in messages) {
            if (calendar == null || !DateUtil.isSameDay(
                    calendar.time,
                    Date(message.timestamp * 1000)
                )
            ) {
                val localDate =
                    Instant.ofEpochSecond(message.timestamp).atZone(ZoneId.systemDefault())
                        .toLocalDate()
                val dateMessage = DateMessage(localDate)
                calendar = Calendar.getInstance()
                calendar.time = Date(message.timestamp * 1000)
                newListMessages.add(dateMessage)
            }
            newListMessages.add(message)
        }
        return newListMessages
    }

    private fun setupListMessageForStream(messages: List<UserMessage>): List<ChatMessage> {
        val newListMessages = mutableListOf<ChatMessage>()
        var calendar: Calendar? = null
        var topicMessage: TopicMessage? = null
        for (message in messages) {
            if (topicMessage == null || topicMessage.nameTopic != message.nameTopic) {
                topicMessage = TopicMessage(message.nameTopic)
                newListMessages.add(topicMessage)
            }

            if (calendar == null || !DateUtil.isSameDay(
                    calendar.time,
                    Date(message.timestamp * 1000)
                )
            ) {
                val localDate =
                    Instant.ofEpochSecond(message.timestamp).atZone(ZoneId.systemDefault())
                        .toLocalDate()
                val dateMessage = DateMessage(localDate)
                calendar = Calendar.getInstance()
                calendar.time = Date(message.timestamp * 1000)
                newListMessages.add(dateMessage)
            }
            newListMessages.add(message)
        }
        return newListMessages
    }

    fun addReaction(messageId: Long, nameTopic: String, emojiName: String, position: Int) {
        apiClient.chatApi.addReaction(messageId, emojiName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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
        apiClient.chatApi.removeReaction(messageId, emojiName, emojiCode, reactionType)
            .doOnComplete {
                chatDao.deleteReaction(userId, emojiCode)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                getMessage(messageId, position, nameTopic)
            }, {
                viewState.showErrorRemoveReaction()
            })
            .addTo(compositeDisposable)
    }

   private fun deleteMessage(idMessage: Long){
        apiClient.chatApi.deleteMessage(idMessage)
            .subscribeOn(Schedulers.io())
            .doOnComplete {
                chatDao.deleteMessage(idMessage)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewState.showDeleteMessageSuccess()
            },{
                viewState.showErrorDeleteMessage()
            })
            .addTo(compositeDisposable)
   }

    private fun editMessage(messageId: Long, nameTopic: String, message: String ){
        apiClient.chatApi.editMessage(messageId, nameTopic, message)
            .subscribeOn(Schedulers.io())
            .doOnComplete {
                chatDao.updateMessage(messageId, message, nameTopic)
            }
            .observeOn(AndroidSchedulers.mainThread())
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

