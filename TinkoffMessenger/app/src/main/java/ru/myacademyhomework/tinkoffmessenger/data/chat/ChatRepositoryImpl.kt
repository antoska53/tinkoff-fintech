package ru.myacademyhomework.tinkoffmessenger.data.chat

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.myacademyhomework.tinkoffmessenger.data.ChatMessage
import ru.myacademyhomework.tinkoffmessenger.data.database.ChatDao
import ru.myacademyhomework.tinkoffmessenger.data.database.model.MessageDb
import ru.myacademyhomework.tinkoffmessenger.data.database.model.TopicDb
import ru.myacademyhomework.tinkoffmessenger.data.database.model.UserDb
import ru.myacademyhomework.tinkoffmessenger.data.network.model.MessageResponse
import ru.myacademyhomework.tinkoffmessenger.data.network.model.SendMessageResponse
import ru.myacademyhomework.tinkoffmessenger.data.network.model.UserDto
import ru.myacademyhomework.tinkoffmessenger.data.network.model.UserMessage
import ru.myacademyhomework.tinkoffmessenger.di.ApiClient
import ru.myacademyhomework.tinkoffmessenger.domain.chat.ChatRepository
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val apiClient: ApiClient,
    private val chatDao: ChatDao
): ChatRepository {
    override fun showPopupMenu(nameStream: String): Single<List<TopicDb>> {
        return chatDao.getTopicsForStream(nameStream)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun initChat(): Flowable<List<UserDto>> {
        TODO("Not yet implemented")
    }

    override fun getOwnUser(): Single<UserDb> {
        TODO("Not yet implemented")
    }

    override fun getOldMessageFromDb(): Single<List<UserMessage>> {
        TODO("Not yet implemented")
    }

    override fun getAllMessagesFromDb(): Flowable<List<UserMessage>> {
        TODO("Not yet implemented")
    }

    override fun getAllMessagesFromDbForStream(): Flowable<List<UserMessage>> {
        TODO("Not yet implemented")
    }

    override fun getMessages(anchor: String): Single<List<MessageDb>> {
        TODO("Not yet implemented")
    }

    override fun getMessagesForStream(anchor: String): Single<MutableList<List<MessageDb>>> {
        TODO("Not yet implemented")
    }

    override fun getMessage(
        messageId: Long,
        position: Int,
        nameTopic: String?
    ): Single<MessageResponse> {
        TODO("Not yet implemented")
    }

    override fun sendMessage(message: String, nameTopic: String): Single<SendMessageResponse> {
        TODO("Not yet implemented")
    }

    override fun updateEmoji(
        emoji: String,
        idMessage: Long,
        nameTopic: String,
        position: Int
    ): Completable {
        TODO("Not yet implemented")
    }

    override fun setupListMessage(messages: List<UserMessage>): List<ChatMessage> {
        TODO("Not yet implemented")
    }

    override fun setupListMessageForStream(messages: List<UserMessage>): List<ChatMessage> {
        TODO("Not yet implemented")
    }

    override fun addReaction(
        messageId: Long,
        nameTopic: String,
        emojiName: String,
        position: Int
    ): Completable {
        TODO("Not yet implemented")
    }

    override fun removeReaction(
        messageId: Long,
        nameTopic: String,
        emojiName: String,
        emojiCode: String,
        reactionType: String,
        userId: Int,
        position: Int
    ): Completable {
        TODO("Not yet implemented")
    }

    override fun deleteMessage(idMessage: Long): Completable {
        TODO("Not yet implemented")
    }

    override fun editMessage(messageId: Long, nameTopic: String, message: String): Completable {
        TODO("Not yet implemented")
    }

}