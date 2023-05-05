package ru.myacademyhomework.tinkoffmessenger.domain.chat

import javax.inject.Inject

class RemoveReactionUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    fun removeReaction(
        messageId: Long, nameTopic: String, emojiName: String, emojiCode: String,
        reactionType: String, userId: Int, position: Int
    ) = repository.removeReaction(
        messageId,
        nameTopic,
        emojiName,
        emojiCode,
        reactionType,
        userId,
        position
    )
}