package ru.myacademyhomework.tinkoffmessenger.data.mapper

import ru.myacademyhomework.tinkoffmessenger.data.Reaction
import ru.myacademyhomework.tinkoffmessenger.data.database.model.ReactionDb
import ru.myacademyhomework.tinkoffmessenger.data.network.model.ReactionDto
import javax.inject.Inject

class ReactionMapper @Inject constructor() {
    fun mapDbModelToEntity(reactionDb: ReactionDb): Reaction {
        return Reaction(
            reactionDb.emojiCode,
            reactionDb.emojiName,
            reactionDb.reactionType,
            reactionDb.userId
        )
    }

    fun mapDtoToDbModel(reaction: ReactionDto, userId: Long): ReactionDb{
        return ReactionDb(
            reaction.emojiCode,
            reaction.emojiName,
            reaction.reactionType,
            reaction.userId,
            userId
        )
    }
}