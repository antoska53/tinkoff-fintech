package ru.myacademyhomework.tinkoffmessenger.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import ru.myacademyhomework.tinkoffmessenger.data.Item

@Serializable
data class Topic(
    @SerialName("max_id")
    val maxID: Int,

    @SerialName("name")
    val name: String,

    @Transient
    var nameStream: String = ""
) : Item