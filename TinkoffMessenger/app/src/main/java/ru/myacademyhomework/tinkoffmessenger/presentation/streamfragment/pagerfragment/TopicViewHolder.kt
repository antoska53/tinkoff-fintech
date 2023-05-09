package ru.myacademyhomework.tinkoffmessenger.presentation.streamfragment.pagerfragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.presentation.listeners.TopicListener
import ru.myacademyhomework.tinkoffmessenger.domain.pager.Topic

class TopicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val nameTopic: TextView = itemView.findViewById(R.id.textview_name_stream)

    fun onBind(topic: Topic, listener: TopicListener) {
        nameTopic.text = topic.name
        nameTopic.setOnClickListener {
            listener.onClickStream(topic)
        }
    }

    companion object {
        fun createStreamViewHolder(parent: ViewGroup): TopicViewHolder {
            return TopicViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_holder_stream, parent, false)
            )
        }
    }
}