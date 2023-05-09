package ru.myacademyhomework.tinkoffmessenger.domain.util

class SmileFactory {
    companion object{
        private val emojiList: List<String> =
            listOf(
                "\uD83D\uDE00",
                "\uD83D\uDE03",
                "\uD83D\uDE04",
                "\uD83D\uDE01",
                "\uD83D\uDE06",
                "\uD83D\uDE05",
                "\uD83E\uDD23",
                "\uD83D\uDE02",
                "\uD83D\uDE42",
                "\uD83D\uDE43"
            )

        fun createSmile(): List<String> {
            return emojiList
        }
    }
}