package ru.myacademyhomework.tinkoffmessenger.factory

import ru.myacademyhomework.tinkoffmessenger.data.Message
import java.time.LocalDate

class MessageFactory {
    companion object {
        val messages: MutableList<Any> = mutableListOf(
            LocalDate.of(2021, 10, 15),
            Message(
                avatar = "",
                name = "Alice",
                message = "hello world",
                listEmoji = mutableListOf()
            ),
            Message(
                avatar = "",
                name = "Bob",
                message = "hello",
                listEmoji = mutableListOf("\uD83D\uDE04")
            ),
            LocalDate.of(2021, 10, 16),
            Message(
                avatar = "",
                name = "Hack",
                message = "hahahaha",
                listEmoji = mutableListOf("\uD83D\uDE04")
            )
        )
    }
}