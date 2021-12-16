package ru.myacademyhomework.tinkoffmessenger.listeners

interface ChatMessageListener {
    fun itemLongClicked(idMessage: Long, nameTopic: String, position: Int): Boolean
    fun itemAddReactionClicked(messageId: Long, emojiName: String, position: Int)
    fun itemRemoveReactionClicked(messageId: Long, emojiName: String, emojiCode: String, reactionType: String, userId: Int, position: Int)
}