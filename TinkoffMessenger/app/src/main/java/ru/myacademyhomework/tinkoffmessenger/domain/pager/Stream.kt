package ru.myacademyhomework.tinkoffmessenger.domain.pager


data class Stream(
    val nameChannel: String,
    val topics: List<Topic>,
    var isSelected: Boolean = false
) : Item