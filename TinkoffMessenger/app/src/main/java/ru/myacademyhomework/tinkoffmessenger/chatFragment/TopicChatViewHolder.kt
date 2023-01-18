package ru.myacademyhomework.tinkoffmessenger.chatFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.data.TopicMessage
import ru.myacademyhomework.tinkoffmessenger.listeners.TopicChatListener

class TopicChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val textview = itemView.findViewById<TextView>(R.id.textview_topic_chat)

    fun onBind(topicMessage: TopicMessage, topicChatListener: TopicChatListener) {
        textview.text = topicMessage.nameTopic
        textview.setOnClickListener {
            topicChatListener.onClickTopic(topicMessage.nameTopic)
        }
    }

    companion object {
        fun createViewHolder(parent: ViewGroup): TopicChatViewHolder {
            return TopicChatViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_holder_topic_chat, parent, false)
            )
        }
    }
}