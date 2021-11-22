package ru.myacademyhomework.tinkoffmessenger.data

import java.time.LocalDate

data class DateMessage(
    val date: LocalDate
): ChatMessage

