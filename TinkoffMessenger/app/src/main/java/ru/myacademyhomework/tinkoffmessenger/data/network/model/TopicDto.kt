package ru.myacademyhomework.tinkoffmessenger.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import ru.myacademyhomework.tinkoffmessenger.domain.pager.Item

@Serializable
data class TopicDto(
    @Transient
    val streamId: Long = 0,

    @SerialName("name")
    val name: String,

    @Transient
    var nameStream: String = ""
)