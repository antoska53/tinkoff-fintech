package ru.myacademyhomework.tinkoffmessenger.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopicResponse(
    @SerialName("msg")
    val msg: String,

    @SerialName("result")
    val result: String,

    @SerialName("topics")
    val topics: List<Topic>
)

