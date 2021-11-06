package ru.myacademyhomework.tinkoffmessenger.factory

import ru.myacademyhomework.tinkoffmessenger.data.DateMessage
import ru.myacademyhomework.tinkoffmessenger.data.Message
import ru.myacademyhomework.tinkoffmessenger.data.SealedMessage
import java.time.LocalDate

class MessageFactory {
    companion object {
        private val messages: MutableList<SealedMessage> = mutableListOf(
            DateMessage(LocalDate.of(2021, 10, 15)),
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
            DateMessage(LocalDate.of(2021, 10, 16)),
            Message(
                avatar = "",
                name = "Hack",
                message = "hahahaha",
                listEmoji = mutableListOf("\uD83D\uDE04")
            )
        )

        fun createMessage(): MutableList<SealedMessage> {
            return messages
        }
    }
}