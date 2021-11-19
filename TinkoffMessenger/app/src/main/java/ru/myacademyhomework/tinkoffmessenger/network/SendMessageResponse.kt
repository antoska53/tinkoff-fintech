package ru.myacademyhomework.tinkoffmessenger.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SendMessageResponse(
    @SerialName("id")
    val id: Int,

    @SerialName("msg")
    val msg: String,

    @SerialName("result")
    val result: String
)