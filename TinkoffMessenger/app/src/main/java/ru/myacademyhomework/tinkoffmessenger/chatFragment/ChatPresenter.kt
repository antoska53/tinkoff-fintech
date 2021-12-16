package ru.myacademyhomework.tinkoffmessenger.chatFragment

import android.text.Editable
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.toFlowable
import io.reactivex.schedulers.Schedulers
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.common.BasePresenter
import ru.myacademyhomework.tinkoffmessenger.data.ChatMessage
import ru.myacademyhomework.tinkoffmessenger.data.DateMessage
import ru.myacademyhomework.tinkoffmessenger.data.Emoji
import ru.myacademyhomework.tinkoffmessenger.data.TopicMessage
import ru.myacademyhomework.tinkoffmessenger.database.*
import ru.myacademyhomework.tinkoffmessenger.di.ApiClient
import ru.myacademyhomework.tinkoffmessenger.di.chat.ChatScope
import ru.myacademyhomework.tinkoffmessenger.network.*
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
                    User(
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
                .subscribe {
                    viewState.addRecyclerData(setupListMessage(it))
                    isLoading = false
                }.addTo(compositeDisposable)
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
                        Log.d("LOAD", "getAllMessagesFromDb: LOAD")
                        if (it.isEmpty()) {
                            viewState.showRefresh()
                        } else {
                            Log.d("LOAD", "getAllMessagesFromDb: UPDATE RECYCLER")
                            databaseIsNotEmpty = true
                            viewState.updateRecyclerData(setupListMessage(it))
                            isLoading = false
                        }
                        if (!databaseIsRefresh){
                            Log.d("LOAD", "getAllMessagesFromDb: REFRESH DB")
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
                        firstMessageId = it.first().id.toString()
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        if (!databaseIsNotEmpty) {
                            viewState.showRefresh()
                        }
                        isLoading = true
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
        Log.d("LOAD", "getMessagesForStream: LOAD")
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
                    Log.d("LOAD", "getMessagesForStream: doOnSuc")
                    chatDao.insertMessages(it.flatten())
//                    if (isLoading) getOldMessageFromDb()
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    if (!databaseIsNotEmpty) {
                        viewState.showRefresh()
                    }
//                    isLoading = true
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
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (position == ChatFragment.SEND_MESSAGE_POSITION) {
                            val message = it.messages[0]
                            message.nameTopic = nameTopic
                            viewState.updateMessage(message, this.nameTopic == ChatFragment.STREAM_CHAT)
                        } else {
                            viewState.updateMessage(it.messages[0], position)
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

    fun addReaction(messageId: Long, emojiName: String, position: Int) {
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

    fun removeReaction(
        messageId: Long,
        emojiName: String,
        emojiCode: String,
        reactionType: String,
        userId: Int,
        position: Int
    ) {
        apiClient.chatApi.removeReaction(
            messageId,
            emojiName,
            emojiCode,
            reactionType
        )
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

}

