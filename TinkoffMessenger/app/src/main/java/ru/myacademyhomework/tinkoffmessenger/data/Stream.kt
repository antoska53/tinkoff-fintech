package ru.myacademyhomework.tinkoffmessenger.data

import ru.myacademyhomework.tinkoffmessenger.data.network.model.TopicDto

data class Stream(
    val nameChannel: String,
    val topics: List<TopicDto>,
    var isSelected: Boolean = false
) : Item