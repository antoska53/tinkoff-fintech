package ru.myacademyhomework.tinkoffmessenger.presentation.chatfragment

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.myacademyhomework.tinkoffmessenger.listeners.ChatMessageListener
import ru.myacademyhomework.tinkoffmessenger.data.ChatMessage
import ru.myacademyhomework.tinkoffmessenger.data.DateMessage
import ru.myacademyhomework.tinkoffmessenger.data.TopicMessage
import ru.myacademyhomework.tinkoffmessenger.listeners.TopicChatListener
import ru.myacademyhomework.tinkoffmessenger.data.network.model.UserMessage
import java.lang.IllegalArgumentException
import java.time.Instant
import java.time.ZoneId
import java.util.*

class ChatAdapter(private val listener: ChatMessageListener, private val userId: Int, val topicChatListener: TopicChatListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val messages: MutableList<ChatMessage> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ChatFragment.TYPE_DATE -> DateViewHolder.createViewHolder(parent)
            ChatFragment.TYPE_MESSAGE -> ChatViewHolder.createViewHolder(parent)
            ChatFragment.TYPE_TOPIC -> TopicChatViewHolder.createViewHolder(parent)
            else -> throw IllegalArgumentException()
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when (messages[position]) {
            is UserMessage -> ChatFragment.TYPE_MESSAGE
            is DateMessage -> ChatFragment.TYPE_DATE
            is TopicMessage -> ChatFragment.TYPE_TOPIC
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ChatViewHolder -> holder.onBind(messages[position] as UserMessage, userId, listener)
            is DateViewHolder -> holder.onBind(messages[position] as DateMessage)
            is TopicChatViewHolder -> holder.onBind(messages[position] as TopicMessage, topicChatListener)
        }
    }

    override fun getItemCount(): Int = messages.size


    fun updateMessage(message: UserMessage, isStreamChat: Boolean) {
        val lastMessage = messages.last() as UserMessage
        if (isStreamChat){
            if (message.nameTopic != lastMessage.nameTopic) {
                messages.add(TopicMessage(message.nameTopic))
            }
        }
        if(!DateUtil.isSameDay(Date(lastMessage.timestamp * 1000), Date(message.timestamp * 1000))){
            val localDate =
                Instant.ofEpochSecond(message.timestamp).atZone(ZoneId.systemDefault())
                    .toLocalDate()
            val dateMessage = DateMessage(localDate)
            this.messages.add(dateMessage)
            notifyItemInserted(this.messages.size)
        }
            this.messages.add(message)
            notifyItemInserted(this.messages.size)
    }

    fun updateListEmoji(message: UserMessage, position: Int) {
        messages[position] = message
        notifyItemChanged(position)
    }

    fun updateData(messages: List<ChatMessage>) {
        this.messages.clear()
        this.messages.addAll(messages)
    }

    fun addData(messages: List<ChatMessage>) {
        this.messages.addAll(0, messages)
        notifyItemRangeInserted(0, messages.size)
    }


}