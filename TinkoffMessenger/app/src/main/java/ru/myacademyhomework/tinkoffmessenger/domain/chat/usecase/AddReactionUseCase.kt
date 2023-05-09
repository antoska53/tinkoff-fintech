package ru.myacademyhomework.tinkoffmessenger.domain.chat.usecase

import ru.myacademyhomework.tinkoffmessenger.domain.chat.ChatRepository
import javax.inject.Inject

class AddReactionUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    fun addReaction(messageId: Long, nameTopic: String, emojiName: String, position: Int) =
        repository.addReaction(messageId, nameTopic, emojiName, position)
}