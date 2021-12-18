package ru.myacademyhomework.tinkoffmessenger.listeners

interface ChatMessageListener {
    fun itemLongClicked(idMessage: Long, nameTopic: String, message: String, position: Int): Boolean
    fun itemAddReactionClicked(messageId: Long, nameTopic: String, emojiName: String, position: Int)
    fun itemRemoveReactionClicked(messageId: Long, nameTopic: String, emojiName: String, emojiCode: String, reactionType: String, userId: Int, position: Int)
    fun plusButtonClicked(idMessage: Long, nameTopic: String, position: Int): Boolean
}