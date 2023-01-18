package ru.myacademyhomework.tinkoffmessenger.data

import ru.myacademyhomework.tinkoffmessenger.network.Topic

data class Stream(
    val nameChannel: String,
    val topics: List<Topic>,
    var isSelected: Boolean = false
) : Item