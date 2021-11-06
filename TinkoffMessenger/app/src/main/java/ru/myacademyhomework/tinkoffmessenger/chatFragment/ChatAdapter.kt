package ru.myacademyhomework.tinkoffmessenger.chatFragment

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.myacademyhomework.tinkoffmessenger.ChatMessageListener
import ru.myacademyhomework.tinkoffmessenger.data.Message
import ru.myacademyhomework.tinkoffmessenger.factory.MessageFactory
import java.lang.IllegalArgumentException
import java.time.LocalDate

class ChatAdapter(private val listener: ChatMessageListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val messages: MutableList<Any> = MessageFactory.createMessage()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ChatFragment.TYPE_DATE -> DateViewHolder.createViewHolder(parent)
            ChatFragment.TYPE_MESSAGE -> ChatViewHolder.createViewHolder(parent)
            else -> throw IllegalArgumentException()
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when (messages[position]) {
            is Message -> ChatFragment.TYPE_MESSAGE
            is LocalDate -> ChatFragment.TYPE_DATE
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ChatViewHolder -> holder.onBind(messages[position] as Message, listener)
            is DateViewHolder -> holder.onBind(messages[position] as LocalDate)
        }
    }

    override fun getItemCount(): Int = messages.size


    fun updateData(message: Message) {
        messages.add(message)
        notifyItemInserted(messages.size)
    }

    fun updateListEmoji(position: Int) {
        notifyItemChanged(position)
    }
}