package ru.myacademyhomework.tinkoffmessenger.data.mapper

import android.text.Html
import ru.myacademyhomework.tinkoffmessenger.domain.chat.Reaction
import ru.myacademyhomework.tinkoffmessenger.domain.chat.UserMessage
import ru.myacademyhomework.tinkoffmessenger.data.database.model.MessageDb
import ru.myacademyhomework.tinkoffmessenger.data.network.model.UserMessageDto
import javax.inject.Inject

class UserMessageMapper @Inject constructor() {
    fun mapDbModelToEntity(messageDb: MessageDb, reactions: List<Reaction>): UserMessage {
        return UserMessage(
            avatarURL = messageDb.avatarURL,
            content = messageDb.content,
            id = messageDb.id,
            isMeMessage = messageDb.isMeMessage,
            senderFullName = messageDb.senderFullName,
            timestamp = messageDb.timestamp,
            streamID = messageDb.streamID,
            nameTopic = messageDb.nameTopic,
            reactions = reactions
        )
    }

    fun mapDtoToDbModel(userMessage: UserMessageDto, nameTopic: String, nameStream: String): MessageDb{
        return MessageDb(
            avatarURL = userMessage.avatarURL,
            content = Html.fromHtml(userMessage.content, Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH).trim().toString(),
            id = userMessage.id,
            isMeMessage = userMessage.isMeMessage,
            senderFullName = userMessage.senderFullName,
            timestamp = userMessage.timestamp,
            streamID = userMessage.streamID,
            nameTopic = nameTopic,
            nameStream = nameStream
        )
    }

}