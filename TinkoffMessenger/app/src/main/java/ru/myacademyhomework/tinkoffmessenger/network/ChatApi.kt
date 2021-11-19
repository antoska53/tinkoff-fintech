package ru.myacademyhomework.tinkoffmessenger.network

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.*

interface ChatApi {

    @GET("users/me/subscriptions")
    fun getStreams(): Single<StreamsSubscribeResponse>

    @GET("messages")
    fun getMessages(
        @Query("anchor") anchor: String,
        @Query("num_after") num_after: Int,
        @Query("num_before") num_before: Int,
        @Query("narrow") narrow: String
    ): Single<MessageResponse>

    @GET("users/me/{stream_id}/topics")
    fun getTopics(@Path("stream_id") stream_id: Int): Observable<TopicResponse>

    @POST("messages")
    fun sendMessage(
        @Query("type") type: String,
        @Query("to") to: String,
        @Query("subject") subject: String,
        @Query("content") content: String
    ): Single<SendMessageResponse>

    @POST("messages/{message_id}/reactions")
    fun addReaction(
        @Path("message_id") messageId: Int,
        @Query("emoji_name") emojiName: String,
    ): Completable

    @DELETE("messages/{message_id}/reactions")
    fun removeReaction(
        @Path("message_id") messageId: Int,
        @Query("emoji_name") emojiName: String,
        @Query("emoji_code") emojiCode: String,
        @Query("reaction_type") reactionType: String
    ): Completable

    @GET("users/{user_id}")
    fun getUser(@Path("user_id") userId: Int): Single<UserResponse>

    @GET("users/me")
    fun getOwnUser(): Single<User>

    @GET("users")
    fun getAllUsers(): Single<UsersResponse>

    @GET("users/{user_id_or_email}/presence")
    fun getUserPresence(@Path("user_id_or_email") userId: Int): Single<PresenceResponse>
}