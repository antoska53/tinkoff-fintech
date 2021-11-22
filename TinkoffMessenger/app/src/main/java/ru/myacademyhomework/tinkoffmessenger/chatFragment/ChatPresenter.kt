package ru.myacademyhomework.tinkoffmessenger.chatFragment

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import ru.myacademyhomework.tinkoffmessenger.common.BasePresenter
import ru.myacademyhomework.tinkoffmessenger.data.Emoji
import ru.myacademyhomework.tinkoffmessenger.database.*
import ru.myacademyhomework.tinkoffmessenger.network.Reaction
import ru.myacademyhomework.tinkoffmessenger.network.RetrofitModule
import ru.myacademyhomework.tinkoffmessenger.network.User
import ru.myacademyhomework.tinkoffmessenger.network.UserMessage

class ChatPresenter(
    private val view: ChatView,
    private val chatDao: ChatDao,
    private val nameStream: String,
    private val nameTopic: String,
    private var foundOldest: Boolean
) : BasePresenter() {

    private var databaseIsRefresh = false
    private var databaseIsNotEmpty = false
    private var isLoading = true
    private var firstMessageId = "-1"

    fun initRecycler() {
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
                    view.hideError()
                    view.initRecycler(it)
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
        RetrofitModule.chatApi.getOwnUser()
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
                view.hideError()
            }, {
                view.showError()
            })
            .addTo(compositeDisposable)
    }

    private fun getOldMessageFromDb() {
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
                view.addRecyclerData(it)
                isLoading = false
            }.addTo(compositeDisposable)

    }

    private fun getAllMessagesFromDb() {
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
                    view.showRefresh()
                } else {
                    databaseIsNotEmpty = true
                    view.updateRecyclerData(it)
                    isLoading = false
                }
                if (!databaseIsRefresh) getMessages("newest")
            }, {
            })
            .addTo(compositeDisposable)
    }

    fun getMessages() {
        getMessages(firstMessageId)
    }

    fun getMessages(anchor: String) {
        val chatApi = RetrofitModule.chatApi
        chatApi.getMessages(
            anchor = anchor,
            num_after = 0,
            num_before = 20,
            narrow = "[{\"operand\":\"$nameStream\", \"operator\":\"stream\"},{\"operand\":\"$nameTopic\",\"operator\":\"topic\"}]"
        )
            .subscribeOn(Schedulers.io())
            .map {
                if (it.foundOldest) {
                    foundOldest = it.foundOldest
                    view.addToSharedpref(foundOldest)
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
                    view.showRefresh()
                }
                isLoading = true
            }
            .doOnTerminate {
                view.hideRefresh()
            }
            .subscribe({
                view.showRecycler()
            }, {
                if (databaseIsNotEmpty) {
                    view.showErrorUpdateData()
                } else {
                    view.showError()
                }
                isLoading = false
            })
            .addTo(compositeDisposable)
    }

    private fun getMessage(messageId: Long, position: Int) {
        val chatApi = RetrofitModule.chatApi
        chatApi.getMessages(
            "newest",
            1,
            1,
            "[{\"operand\":\"$nameStream\", \"operator\":\"stream\"},{\"operand\":\"$nameTopic\",\"operator\":\"topic\"},{\"operand\":\"$messageId\",\"operator\":\"id\"}]"
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (position == ChatFragment.SEND_MESSAGE_POSITION) {
                    view.updateMessage(it.messages[0])
                } else {
                    view.updateMessage(it.messages[0], position)
                }
            }, {
            })
            .addTo(compositeDisposable)
    }

    fun sendMessage(message: String) {
        RetrofitModule.chatApi.sendMessage("stream", nameStream, nameTopic, message)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    view.clearEditText()
                    getMessage(it.id, ChatFragment.SEND_MESSAGE_POSITION)
                },
                {
                    view.showErrorSendMessage()
                }
            )
            .addTo(compositeDisposable)
    }

    fun updateEmoji(emoji: String, idMessage: Long, position: Int) {
        val emojiName = Emoji.values().find {
            it.unicode == emoji
        }

        RetrofitModule.chatApi.addReaction(idMessage, emojiName?.nameInZulip ?: "")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                getMessage(idMessage, position)
            },
                {
                    view.showErrorUpdateEmoji()
                })
            .addTo(compositeDisposable)
    }
}