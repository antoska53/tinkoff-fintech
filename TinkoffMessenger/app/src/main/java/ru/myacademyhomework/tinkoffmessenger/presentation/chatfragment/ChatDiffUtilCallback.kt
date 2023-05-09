package ru.myacademyhomework.tinkoffmessenger.presentation.chatfragment

import androidx.recyclerview.widget.DiffUtil
import ru.myacademyhomework.tinkoffmessenger.domain.chat.ChatMessage
import ru.myacademyhomework.tinkoffmessenger.domain.chat.DateMessage
import ru.myacademyhomework.tinkoffmessenger.domain.chat.TopicMessage
import ru.myacademyhomework.tinkoffmessenger.domain.chat.UserMessage

class ChatDiffUtilCallback(
    private val oldLIst: List<ChatMessage>,
    private val newLIst: List<ChatMessage>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldLIst.size

    override fun getNewListSize(): Int = newLIst.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldChatMessage = oldLIst[oldItemPosition]
        val newChatMessage = newLIst[newItemPosition]
        if (oldChatMessage is UserMessage && newChatMessage is DateMessage)
            return false
        if (oldChatMessage is UserMessage && newChatMessage is UserMessage) {
            return oldChatMessage.id == newChatMessage.id
        }
        if (oldChatMessage is DateMessage && newChatMessage is DateMessage) {
            return oldChatMessage.date == newChatMessage.date
        }
        if(oldChatMessage is TopicMessage && newChatMessage is TopicMessage){
            return oldChatMessage.nameTopic == newChatMessage.nameTopic
        }
        return false
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldChatMessage = oldLIst[oldItemPosition]
        val newChatMessage = newLIst[newItemPosition]
        return if (oldChatMessage is UserMessage && newChatMessage is UserMessage) {
            oldChatMessage == newChatMessage
        } else if(oldChatMessage is DateMessage == newChatMessage is DateMessage){
            oldChatMessage == newChatMessage
        } else{
            (oldChatMessage as TopicMessage) == (newChatMessage as TopicMessage)
        }
    }
}