package ru.myacademyhomework.tinkoffmessenger.chatFragment

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.myacademyhomework.tinkoffmessenger.listeners.ChatMessageListener
import ru.myacademyhomework.tinkoffmessenger.data.ChatMessage
import ru.myacademyhomework.tinkoffmessenger.data.DateMessage
import ru.myacademyhomework.tinkoffmessenger.network.UserMessage
import java.lang.IllegalArgumentException
import java.lang.StringBuilder
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class ChatAdapter(private val listener: ChatMessageListener, private val userId: Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val messages: MutableList<ChatMessage> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ChatFragment.TYPE_DATE -> DateViewHolder.createViewHolder(parent)
            ChatFragment.TYPE_MESSAGE -> ChatViewHolder.createViewHolder(parent)
            else -> throw IllegalArgumentException()
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when (messages[position]) {
            is UserMessage -> ChatFragment.TYPE_MESSAGE
            is DateMessage -> ChatFragment.TYPE_DATE
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ChatViewHolder -> holder.onBind(messages[position] as UserMessage, userId, listener)
            is DateViewHolder -> holder.onBind(messages[position] as DateMessage)
        }
    }

    override fun getItemCount(): Int = messages.size


    fun updateMessage(message: UserMessage) {
        messages.add(message)
        notifyItemInserted(messages.size)
    }

    fun updateListEmoji(message: UserMessage, position: Int) {
        messages[position] = message
        notifyItemChanged(position)
    }

    fun updateData(messages: List<ChatMessage>) {
        this.messages.clear()
        this.messages.addAll(messages)
    }

    fun addData(messages: List<UserMessage>){
//        this.messages.addAll(0, messages)
//        notifyItemRangeInserted(0, messages.size)
//        messages.reversed()
//        val calendar: Calendar = Calendar.getInstance()
//        calendar.time = Date.from((this.messages[0] as DateMessage).date.atStartOfDay().atZone(
//            ZoneId.systemDefault()).toInstant())
        var calendar: Calendar? = null
        var dateCount = 0
        for((index, message) in messages.withIndex()){
            if(calendar == null || !DateUtil.isSameDay(calendar.time, Date(message.timestamp * 1000))){
                val localDate = Instant.ofEpochSecond(message.timestamp).atZone(ZoneId.systemDefault()).toLocalDate()
                val dateMessage = DateMessage(localDate)
                calendar = Calendar.getInstance()
                calendar.time = Date(message.timestamp * 1000)
                this.messages.add(index + dateCount, dateMessage)
                notifyItemInserted(index + dateCount)
                this.messages.add(index + dateCount + 1, message)
                notifyItemInserted(index + dateCount + 1)
                dateCount++
            }else{
                this.messages.add(index + dateCount, message)
                notifyItemInserted(index + dateCount)
            }
        }
    }

    fun setupListMessage(messages: List<UserMessage>){
        var calendar: Calendar? = null
        for (message in messages){
            if(calendar == null || !DateUtil.isSameDay(calendar.time, Date(message.timestamp * 1000))){
                val localDate = Instant.ofEpochSecond(message.timestamp).atZone(ZoneId.systemDefault()).toLocalDate()
                val dateMessage = DateMessage(localDate)
                calendar = Calendar.getInstance()
                calendar.time = Date(message.timestamp * 1000)
                this.messages.add(dateMessage)
                notifyItemInserted(this.messages.size - 1)
                this.messages.add(message)
                notifyItemInserted(this.messages.size - 1)
            }else{
                this.messages.add(message)
                notifyItemInserted(this.messages.size - 1)
            }

        }
    }

}