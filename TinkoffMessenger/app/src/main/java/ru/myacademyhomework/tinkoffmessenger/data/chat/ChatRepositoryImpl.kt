package ru.myacademyhomework.tinkoffmessenger.data.chat

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.myacademyhomework.tinkoffmessenger.data.ChatMessage
import ru.myacademyhomework.tinkoffmessenger.data.DateMessage
import ru.myacademyhomework.tinkoffmessenger.data.Emoji
import ru.myacademyhomework.tinkoffmessenger.data.TopicMessage
import ru.myacademyhomework.tinkoffmessenger.data.UserMessage
import ru.myacademyhomework.tinkoffmessenger.data.database.ChatDao
import ru.myacademyhomework.tinkoffmessenger.data.database.model.MessageDb
import ru.myacademyhomework.tinkoffmessenger.data.database.model.ReactionDb
import ru.myacademyhomework.tinkoffmessenger.data.database.model.TopicDb
import ru.myacademyhomework.tinkoffmessenger.data.mapper.ReactionMapper
import ru.myacademyhomework.tinkoffmessenger.data.mapper.UserMapper
import ru.myacademyhomework.tinkoffmessenger.data.mapper.UserMessageMapper
import ru.myacademyhomework.tinkoffmessenger.data.network.model.MessageResponse
import ru.myacademyhomework.tinkoffmessenger.data.network.model.SendMessageResponse
import ru.myacademyhomework.tinkoffmessenger.di.ApiClient
import ru.myacademyhomework.tinkoffmessenger.di.chat.ChatScope
import ru.myacademyhomework.tinkoffmessenger.domain.chat.ChatRepository
import ru.myacademyhomework.tinkoffmessenger.domain.profile.UserInfo
import ru.myacademyhomework.tinkoffmessenger.presentation.chatfragment.DateUtil
import java.time.Instant
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@ChatScope
class ChatRepositoryImpl @Inject constructor(
    private val apiClient: ApiClient,
    private val chatDao: ChatDao,
    private val userMapper: UserMapper,
    private val userMessageMapper: UserMessageMapper,
    private val reactionMapper: ReactionMapper
) : ChatRepository {
    override fun showPopupMenu(nameStream: String): Single<List<TopicDb>> {
        return chatDao.getTopicsForStream(nameStream)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun initChat(): Flowable<List<UserInfo>> {
        return chatDao.getOwnUser()
            .map {
                Log.d("OWN", "initChat: $it")
                it.map { userDb ->
                    userMapper.mapDbModelToEntity(userDb)
//                    UserDto(
//                        avatarURL = userDb.avatarURL,
//                        email = userDb.email,
//                        fullName = userDb.fullName,
//                        userID = userDb.userID
//                    )
                }

            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getOwnUser(): Single<UserInfo> {
        Log.d("OWN", "getOwnUser: OWN")
        return apiClient.chatApi.getOwnUser()
            .subscribeOn(Schedulers.io())
            .doOnSuccess {
                Log.d("OWN", "getOwnUser: UserDto =  $it")
                chatDao.insertOwnUser(userMapper.mapDtoToDbModel(it))
            }
            .map {
                userMapper.mapDtoToEntity(it)
//                UserDb(
//                    avatarURL = it.avatarURL,
//                    email = it.email,
//                    fullName = it.fullName,
//                    userID = it.userID,
//                    isOwn = true
//                )
            }
//            .doOnSuccess {
//                chatDao.insertOwnUser(it)
//            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getOldMessageFromDb(
        nameTopic: String,
        firstMessageId: Long
    ): Single<List<UserMessage>> {
        return chatDao.getOldMessages(nameTopic, firstMessageId)
            .map {
                it.map { messageDb ->
                    userMessageMapper.mapDbModelToEntity(
                        messageDb,
                        chatDao.getReaction(messageDb.id)
                            .map { reactionDb -> reactionMapper.mapDbModelToEntity(reactionDb) }
                    )
//                    UserMessage(
//                        avatarURL = messageDb.avatarURL,
//                        content = messageDb.content,
//                        id = messageDb.id,
//                        isMeMessage = messageDb.isMeMessage,
//                        senderFullName = messageDb.senderFullName,
//                        timestamp = messageDb.timestamp,
//                        streamID = messageDb.streamID,
//                        reactions = chatDao.getReaction(messageDb.id).map { reactionDb ->
//                            ReactionDto(
//                                reactionDb.emojiCode,
//                                reactionDb.emojiName,
//                                reactionDb.reactionType,
//                                reactionDb.userId
//                            )
//                        }
//                    )
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getAllMessagesFromDb(
        nameTopic: String,
        nameStream: String
    ): Flowable<List<UserMessage>> {
        return chatDao.getAllMessages(nameTopic, nameStream)
            .map {
                it.map { messageDb ->
                    userMessageMapper.mapDbModelToEntity(
                        messageDb,
                        chatDao.getReaction(messageDb.id)
                            .map { reactionDb -> reactionMapper.mapDbModelToEntity(reactionDb) }
                    )
//                    UserMessageDto(
//                        avatarURL = messageDb.avatarURL,
//                        content = messageDb.content,
//                        id = messageDb.id,
//                        isMeMessage = messageDb.isMeMessage,
//                        senderFullName = messageDb.senderFullName,
//                        timestamp = messageDb.timestamp,
//                        streamID = messageDb.streamID,
//                        nameTopic = nameTopic,
//                        reactions = chatDao.getReaction(messageDb.id).map { reactionDb ->
//                            ReactionDto(
//                                reactionDb.emojiCode,
//                                reactionDb.emojiName,
//                                reactionDb.reactionType,
//                                reactionDb.userId
//                            )
//                        }
//                    )
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getAllMessagesFromDbForStream(nameStream: String): Flowable<List<UserMessage>> {
        return chatDao.getAllMessagesForStream(nameStream)
            .map {
                it.map { messageDb ->
                    userMessageMapper.mapDbModelToEntity(
                        messageDb,
                        chatDao.getReaction(messageDb.id).map { reactionMapper.mapDbModelToEntity(it) }
                    )
//                    UserMessageDto(
//                        avatarURL = messageDb.avatarURL,
//                        content = messageDb.content,
//                        id = messageDb.id,
//                        isMeMessage = messageDb.isMeMessage,
//                        senderFullName = messageDb.senderFullName,
//                        timestamp = messageDb.timestamp,
//                        streamID = messageDb.streamID,
//                        nameTopic = messageDb.nameTopic,
//                        reactions = chatDao.getReaction(messageDb.id).map { reactionDb ->
//                            ReactionDto(
//                                reactionDb.emojiCode,
//                                reactionDb.emojiName,
//                                reactionDb.reactionType,
//                                reactionDb.userId
//                            )
//                        }
//                    )
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getMessages(
        anchor: String,
        nameStream: String,
        nameTopic: String
    ): Single<MessageResponse> {
        return apiClient.chatApi.getMessages(
            anchor = anchor,
            num_after = 0,
            num_before = 20,
            narrow = "[{\"operand\":\"$nameStream\", \"operator\":\"stream\"},{\"operand\":\"$nameTopic\",\"operator\":\"topic\"}]"
        )
            .subscribeOn(Schedulers.io())
            .doOnSuccess {
                val listMessages = it.messages.map { userMessage ->
                    chatDao.insertReaction(
                        userMessage.reactions.map { reaction ->
                            reactionMapper.mapDtoToDbModel(reaction, userMessage.id)
//                            ReactionDb(
//                                reaction.emojiCode,
//                                reaction.emojiName,
//                                reaction.reactionType,
//                                reaction.userId,
//                                userMessage.id
//                            )
                        })
                    userMessageMapper.mapDtoToDbModel(userMessage, nameTopic, nameStream)
//                    MessageDb(
//                        avatarURL = userMessage.avatarURL,
//                        content = userMessage.content,
//                        id = userMessage.id,
//                        isMeMessage = userMessage.isMeMessage,
//                        senderFullName = userMessage.senderFullName,
//                        timestamp = userMessage.timestamp,
//                        streamID = userMessage.streamID,
//                        nameTopic = nameTopic,
//                        nameStream = nameStream
//                    )
                }
                chatDao.insertMessages(listMessages)
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getMessagesForStream(
        anchor: String,
        nameStream: String
    ): Single<MutableList<List<MessageDb>>> {
        return chatDao.getTopicsForStream(nameStream)
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
                                    reactionMapper.mapDtoToDbModel(reaction, userMessage.id)
//                                    ReactionDb(
//                                        reaction.emojiCode,
//                                        reaction.emojiName,
//                                        reaction.reactionType,
//                                        reaction.userId,
//                                        userMessage.id
//                                    )
                                })
                            userMessageMapper.mapDtoToDbModel(userMessage, topicDb.nameTopic, nameStream)
//                            MessageDb(
//                                avatarURL = userMessage.avatarURL,
//                                content = userMessage.content,
//                                id = userMessage.id,
//                                isMeMessage = userMessage.isMeMessage,
//                                senderFullName = userMessage.senderFullName,
//                                timestamp = userMessage.timestamp,
//                                streamID = userMessage.streamID,
//                                nameTopic = topicDb.nameTopic,
//                                nameStream = nameStream
//                            )
                        }
                    }
            }
            .toList()
            .doOnSuccess {
//                    databaseIsRefresh = true
//                    databaseIsNotEmpty = true
                chatDao.insertMessages(it.flatten())
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getMessage(
        messageId: Long,
        position: Int,
        nameTopic: String,
        nameStream: String?
    ): Single<MessageResponse> {
        return apiClient.chatApi.getMessages(
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
                        reactionMapper.mapDtoToDbModel(reaction, message.id)
//                        ReactionDb(
//                            reaction.emojiCode,
//                            reaction.emojiName,
//                            reaction.reactionType,
//                            reaction.userId,
//                            message.id
//                        )
                    })
                chatDao.insertMessage(
                    userMessageMapper.mapDtoToDbModel(message, message.nameTopic, nameStream!!)
//                    MessageDb(
//                        avatarURL = message.avatarURL,
//                        content = message.content,
//                        id = message.id,
//                        isMeMessage = message.isMeMessage,
//                        senderFullName = message.senderFullName,
//                        timestamp = message.timestamp,
//                        streamID = message.streamID,
//                        nameTopic = message.nameTopic,
//                        nameStream = nameStream!!
//                    )
                )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun sendMessage(
        message: String,
        nameTopic: String,
        nameStream: String
    ): Single<SendMessageResponse> {
        return apiClient.chatApi.sendMessage("stream", nameStream, nameTopic, message)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun updateEmoji(
        emoji: String,
        idMessage: Long,
        nameTopic: String,
        position: Int
    ): Completable {
        val emojiName = Emoji.values().find {
            it.unicode == emoji
        }

        return apiClient.chatApi.addReaction(idMessage, emojiName?.nameInZulip ?: "")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun setupListMessage(messages: List<UserMessage>): List<ChatMessage> {
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

    override fun setupListMessageForStream(messages: List<UserMessage>): List<ChatMessage> {
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

    override fun addReaction(
        messageId: Long,
        nameTopic: String,
        emojiName: String,
        position: Int
    ): Completable {
        return apiClient.chatApi.addReaction(messageId, emojiName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun removeReaction(
        messageId: Long,
        emojiName: String,
        emojiCode: String,
        reactionType: String,
        userId: Int
    ): Completable {
        return apiClient.chatApi.removeReaction(messageId, emojiName, emojiCode, reactionType)
            .doOnComplete {
                chatDao.deleteReaction(userId, emojiCode)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun deleteMessage(idMessage: Long): Completable {
        return apiClient.chatApi.deleteMessage(idMessage)
            .subscribeOn(Schedulers.io())
            .doOnComplete {
                chatDao.deleteMessage(idMessage)
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun editMessage(messageId: Long, nameTopic: String, message: String): Completable {
        return apiClient.chatApi.editMessage(messageId, nameTopic, message)
            .subscribeOn(Schedulers.io())
            .doOnComplete {
                chatDao.updateMessage(messageId, message, nameTopic)
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

}