package ru.myacademyhomework.tinkoffmessenger.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable
import ru.myacademyhomework.tinkoffmessenger.network.UserMessage

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

    @Query("SELECT * FROM message_table WHERE stream_id = :streamId")
    fun getMessages(streamId: Long): Flowable<List<MessageDb>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReaction(messages: List<ReactionDb>)

    @Query("SELECT * FROM reaction_table WHERE message_id = :messageId")
    fun getReaction(messageId: Long): List<ReactionDb>
}