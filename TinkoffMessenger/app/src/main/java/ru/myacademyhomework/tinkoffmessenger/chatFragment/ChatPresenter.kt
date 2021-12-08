package ru.myacademyhomework.tinkoffmessenger.chatFragment

import android.text.Editable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.common.BasePresenter
import ru.myacademyhomework.tinkoffmessenger.data.Emoji
import ru.myacademyhomework.tinkoffmessenger.database.*
import ru.myacademyhomework.tinkoffmessenger.di.ApiClient
import ru.myacademyhomework.tinkoffmessenger.di.chat.ChatScope
import ru.myacademyhomework.tinkoffmessenger.network.*
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

    fun loadFoundOldest(foundOldest: Boolean) {
        this.foundOldest = foundOldest
    }

    fun buttonSendMessageSetImage(str: String) {
        if (str.isNotEmpty()) viewState.buttonSendMessageSetImage(R.drawable.ic_plane)
        else viewState.buttonSendMessageSetImage(R.drawable.ic_cross)
    }

    fun onClickButtonSendMessage(text: Editable) {
        if (text.isNotEmpty()) {
            sendMessage(message = text.toString())
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
                    getAllMessagesFromDb()
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
        nameTopic?.let {nameTopic ->
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
                    viewState.addRecyclerData(it)
                    isLoading = false
                }.addTo(compositeDisposable)
        }
    }

    private fun getAllMessagesFromDb() {
        nameTopic?.let {nameTopic ->
            chatDao.getAllMessages(nameTopic)
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
                    if (it.isEmpty()) {
                        viewState.showRefresh()
                    } else {
                        databaseIsNotEmpty = true
                        viewState.updateRecyclerData(it)
                        isLoading = false
                    }
                    if (!databaseIsRefresh) getMessages("newest")
                }, {
                })
                .addTo(compositeDisposable)
        }
    }

    private fun getMessages() {
        getMessages(firstMessageId)
    }

    private fun getMessages(anchor: String) {
        nameTopic?.let {nameTopic ->
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
                            nameTopic = nameTopic
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

    private fun getMessage(messageId: Long, position: Int) {
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
                    viewState.updateMessage(it.messages[0])
                } else {
                    viewState.updateMessage(it.messages[0], position)
                }
            }, {
            })
            .addTo(compositeDisposable)
    }

    private fun sendMessage(message: String) {
        nameStream?.let { nameStream ->
            nameTopic?.let { nameTopic ->
                apiClient.chatApi.sendMessage("stream", nameStream, nameTopic, message)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            viewState.clearEditText()
                            getMessage(it.id, ChatFragment.SEND_MESSAGE_POSITION)
                        },
                        {
                            viewState.showErrorSendMessage()
                        }
                    )
                    .addTo(compositeDisposable)
            }
        }
    }

    fun updateEmoji(emoji: String, idMessage: Long, position: Int) {
        val emojiName = Emoji.values().find {
            it.unicode == emoji
        }

        apiClient.chatApi.addReaction(idMessage, emojiName?.nameInZulip ?: "")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                getMessage(idMessage, position)
            },
                {
                    viewState.showErrorUpdateEmoji()
                })
            .addTo(compositeDisposable)
    }
}
