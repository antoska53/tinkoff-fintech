package ru.myacademyhomework.tinkoffmessenger.domain.chat

import java.time.LocalDate

data class DateMessage(
    val date: LocalDate
): ChatMessage

