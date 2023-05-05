package ru.myacademyhomework.tinkoffmessenger.domain.chat

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import ru.myacademyhomework.tinkoffmessenger.data.ChatMessage
import ru.myacademyhomework.tinkoffmessenger.data.database.model.MessageDb
import ru.myacademyhomework.tinkoffmessenger.data.database.model.TopicDb
import ru.myacademyhomework.tinkoffmessenger.data.database.model.UserDb
import ru.myacademyhomework.tinkoffmessenger.data.network.model.MessageResponse
import ru.myacademyhomework.tinkoffmessenger.data.network.model.SendMessageResponse
import ru.myacademyhomework.tinkoffmessenger.data.network.model.UserDto
import ru.myacademyhomework.tinkoffmessenger.data.network.model.UserMessage

interface ChatRepository {
    fun showPopupMenu(nameStream: String): Single<List<TopicDb>>
    fun initChat(): Flowable<List<UserDto>>
    fun getOwnUser(): Single<UserDb>
    fun getOldMessageFromDb(): Single<List<UserMessage>>
    fun getAllMessagesFromDb(): Flowable<List<UserMessage>>
    fun getAllMessagesFromDbForStream(): Flowable<List<UserMessage>>
    fun getMessages(anchor: String): Single<List<MessageDb>>
    fun getMessagesForStream(anchor: String): Single<MutableList<List<MessageDb>>>
    fun getMessage(messageId: Long, position: Int, nameTopic: String?): Single<MessageResponse>
    fun sendMessage(message: String, nameTopic: String): Single<SendMessageResponse>
    fun updateEmoji(emoji: String, idMessage: Long, nameTopic: String, position: Int): Completable
    fun setupListMessage(messages: List<UserMessage>): List<ChatMessage>
    fun setupListMessageForStream(messages: List<UserMessage>): List<ChatMessage>
    fun addReaction(messageId: Long, nameTopic: String, emojiName: String, position: Int): Completable
    fun removeReaction(messageId: Long, nameTopic: String, emojiName: String, emojiCode: String,
                       reactionType: String, userId: Int, position: Int):Completable
    fun deleteMessage(idMessage: Long): Completable
    fun editMessage(messageId: Long, nameTopic: String, message: String ): Completable
}