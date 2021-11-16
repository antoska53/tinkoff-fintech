package ru.myacademyhomework.tinkoffmessenger.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface ChatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStream(streams: List<StreamDb>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTopics(topics: List<TopicDb>)

    @Query("SELECT * FROM stream_table")
    fun getStreams(): Flowable<List<StreamDb>>

    @Query("SELECT * FROM topic_table WHERE name_stream = :nameStream")
    fun getTopics(nameStream: String): List<TopicDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessages(messages: List<MessageDb>)

    @Query("SELECT * FROM message_table WHERE name_topic = :nameTopic")
    fun getMessages(nameTopic: String): Flowable<List<MessageDb>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReaction(messages: List<ReactionDb>)

    @Query("SELECT * FROM reaction_table WHERE message_id = :messageId")
    fun getReaction(messageId: Long): List<ReactionDb>

    @Query("SELECT * FROM user_table WHERE is_own = 1")
    fun getOwnUser(): Flowable<List<UserDb>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOwnUser(user: UserDb)

    @Query("SELECT * FROM user_table")
    fun getAllUsers(): Flowable<List<UserDb>>

    @Query("SELECT * FROM user_table WHERE user_id = :userId")
    fun getUser(userId: Int): Single<UserDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(users: List<UserDb>)
}