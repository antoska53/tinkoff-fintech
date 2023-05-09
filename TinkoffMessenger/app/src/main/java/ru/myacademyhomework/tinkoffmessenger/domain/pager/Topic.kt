package ru.myacademyhomework.tinkoffmessenger.domain.pager

data class Topic(
    val streamId: Long = 0,
    val name: String,
    var nameStream: String = ""
): Item
