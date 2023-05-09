package ru.myacademyhomework.tinkoffmessenger.domain.chat.usecase

import ru.myacademyhomework.tinkoffmessenger.domain.chat.ChatRepository
import javax.inject.Inject

class RemoveReactionUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    fun removeReaction(
        messageId: Long,  emojiName: String, emojiCode: String,
        reactionType: String, userId: Int
    ) = repository.removeReaction(
        messageId,
        emojiName,
        emojiCode,
        reactionType,
        userId
    )
}