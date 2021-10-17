package ru.myacademyhomework.tinkoffmessenger.chatFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.myacademyhomework.tinkoffmessenger.ChatMessageListener
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.data.DateMessage
import ru.myacademyhomework.tinkoffmessenger.data.Message
import java.lang.IllegalArgumentException

class ChatAdapter(private val listener: ChatMessageListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val messages: MutableList<Any> = mutableListOf(
        Message(avatar = "", name = "Alice", message = "hello world", listEmoji = mutableListOf()),
        Message(avatar = "", name = "Bob", message = "hello", listEmoji = mutableListOf("\uD83D\uDE04")),
        DateMessage(month = "октябрь", day = "16"),
        Message(avatar = "", name = "Hack", message = "hahahaha", listEmoji = mutableListOf("\uD83D\uDE04")),
        DateMessage(month = "октябрь", day = "15")
    )


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//       return  ChatViewHolder(
//        LayoutInflater.from(parent.context).inflate(R.layout.view_holder_message, parent, false))
        return when (viewType) {
            ChatFragment.TYPE_DATE -> DateViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_holder_date, parent, false)
            )
            ChatFragment.TYPE_MESSAGE -> ChatViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_holder_message, parent, false)
            )
            else -> throw IllegalArgumentException()
        }

    }

    override fun getItemViewType(position: Int): Int {
        return  when (messages[position]) {
                is Message -> ChatFragment.TYPE_MESSAGE
                else -> ChatFragment.TYPE_DATE
            }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ChatViewHolder -> holder.onBind(messages[position] as Message, listener)
            is DateViewHolder -> holder.onBind(messages[position] as DateMessage)
        }
//        holder.onBind(messages[position])
    }

    override fun getItemCount(): Int = messages.size



    fun updateData(message: Message){
        messages.add(0, message)
        notifyDataSetChanged()
    }

    fun updateListEmoji(emoji: String, position: Int){
        val message = messages[position]
        if (message is Message){
            message.listEmoji.add(emoji)
            notifyDataSetChanged()
        }
    }
}