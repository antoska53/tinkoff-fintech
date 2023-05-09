package ru.myacademyhomework.tinkoffmessenger.domain.chat.usecase

import ru.myacademyhomework.tinkoffmessenger.domain.chat.ChatRepository
import javax.inject.Inject

class UpdateEmojiUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    fun updateEmoji(emoji: String, idMessage: Long, nameTopic: String, position: Int) =
        repository.updateEmoji(emoji, idMessage, nameTopic, position)
}