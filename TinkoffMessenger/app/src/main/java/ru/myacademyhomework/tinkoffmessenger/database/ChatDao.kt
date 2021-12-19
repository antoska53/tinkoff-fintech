package ru.myacademyhomework.tinkoffmessenger.database

import androidx.room.*
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface ChatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStream(streams: List<StreamDb>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTopics(topics: List<TopicDb>)

//    @Query("SELECT * FROM stream_table WHERE nameStream = :nameStream")
//    fun getStream(nameStream: String): Single<StreamDb>

    @Query("SELECT * FROM stream_table")
    fun getAllStreams(): Flowable<List<StreamDb>>

    @Query("SELECT * FROM stream_table WHERE subscribed_status = 1")
    fun getSubscribedStreams(): Flowable<List<StreamDb>>

    @Query("SELECT * FROM topic_table WHERE name_stream = :nameStream")
    fun getTopics(nameStream: String): List<TopicDb>

    @Query("SELECT * FROM topic_table WHERE name_stream = :nameStream")
    fun getTopicsForSearchStream(nameStream: String): Single<List<TopicDb>>

    @Query("SELECT * FROM topic_table WHERE name_stream = :nameStream")
    fun getTopicsForStream(nameStream: String): Single<List<TopicDb>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessages(messages: List<MessageDb>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessage(message: MessageDb)

    @Query("SELECT * FROM message_table WHERE name_topic = :nameTopic AND name_stream = :nameStream")
    fun getAllMessages(nameTopic: String, nameStream: String): Flowable<List<MessageDb>>

    @Query("SELECT * FROM message_table WHERE name_stream = :nameStream")
    fun getAllMessagesForStream( nameStream: String): Flowable<List<MessageDb>>

    @Query("SELECT * FROM message_table WHERE name_topic = :nameTopic AND id < :idMessage LIMIT 20")
    fun getOldMessages(nameTopic: String, idMessage: Long): Flowable<List<MessageDb>>

    @Query("DELETE FROM message_table WHERE id = :messageId")
    fun deleteMessage(messageId: Long)

    @Query("UPDATE message_table SET content = :message,  name_topic = :nameTopic WHERE id = :messageId")
    fun updateMessage(messageId: Long, message: String, nameTopic: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReaction(messages: List<ReactionDb>)

    @Query("SELECT * FROM reaction_table WHERE message_id = :messageId")
    fun getReaction(messageId: Long): List<ReactionDb>

    @Query("DELETE FROM reaction_table where user_id = :userId AND emoji_code = :emojiCode")
    fun deleteReaction(userId: Int, emojiCode: String)

    @Query("SELECT * FROM user_table WHERE is_own = 1")
    fun getOwnUser(): Flowable<List<UserDb>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOwnUser(user: UserDb)

    @Query("SELECT * FROM user_table")
    fun getAllUsers(): Flowable<List<UserDb>>

    @Query("SELECT * FROM user_table WHERE user_id = :userId")
    fun getUser(userId: Int): Single<UserDb>

    @Query("SELECT * FROM user_table WHERE full_name = :name")
    fun getUserForSearch(name: String): UserDb

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(users: List<UserDb>)

    @Query("SELECT * FROM topic_table WHERE name_topic = :nameTopic")
    fun getTopic(nameTopic: String): TopicDb?

    @Query("SELECT * FROM stream_table WHERE nameStream = :nameStream")
    fun getStream(nameStream: String): StreamDb?
}