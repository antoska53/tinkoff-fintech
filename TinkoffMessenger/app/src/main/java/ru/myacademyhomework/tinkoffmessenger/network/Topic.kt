package ru.myacademyhomework.tinkoffmessenger.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import ru.myacademyhomework.tinkoffmessenger.data.Item

@Serializable
data class Topic(
    @Transient
    val streamId: Long = 0,

    @SerialName("name")
    val name: String,

    @Transient
    var nameStream: String = ""
) : Item