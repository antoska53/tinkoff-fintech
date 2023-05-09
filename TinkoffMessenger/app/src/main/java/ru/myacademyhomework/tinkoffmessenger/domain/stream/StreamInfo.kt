package ru.myacademyhomework.tinkoffmessenger.domain.stream


data class StreamInfo(
    val streamId: Long,
    val nameChannel: String,
    var subscribedStatus: Boolean
)