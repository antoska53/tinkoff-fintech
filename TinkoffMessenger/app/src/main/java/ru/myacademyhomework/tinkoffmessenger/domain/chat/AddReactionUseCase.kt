package ru.myacademyhomework.tinkoffmessenger.domain.chat

import javax.inject.Inject

class AddReactionUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    fun addReaction(messageId: Long, nameTopic: String, emojiName: String, position: Int) =
        repository.addReaction(messageId, nameTopic, emojiName, position)
}