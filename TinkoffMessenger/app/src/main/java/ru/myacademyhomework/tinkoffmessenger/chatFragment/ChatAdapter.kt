package ru.myacademyhomework.tinkoffmessenger.chatFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.data.Message
import java.lang.IllegalArgumentException

class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var messages: List<Message> = listOf(
        Message(avatar = "", name = "Alice", message = "hello world", date = "16 октября"),
        Message(avatar = "", name = "Bob", message = "hello", "15 октября"),
        Message(avatar = "", name = "Hack", message = "hahahaha", "15 октября")
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
        return if (position != 0) {
            when (messages[position].date) {
                messages[position - 1].date -> ChatFragment.TYPE_MESSAGE
                else -> ChatFragment.TYPE_DATE
            }
        } else {
            ChatFragment.TYPE_MESSAGE
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ChatViewHolder -> holder.onBind(messages[position])
            is DateViewHolder -> holder.onBind(messages[position])
        }
//        holder.onBind(messages[position])
    }

    override fun getItemCount(): Int = messages.size


}