package ru.myacademyhomework.tinkoffmessenger.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class StreamsSubscribeResponse(
    @SerialName("msg")
    val msg: String,

    @SerialName("result")
    val result: String,

    @SerialName("subscriptions")
    val subscriptions: List<SubscriptionDto>
)
