package ru.myacademyhomework.tinkoffmessenger.streamfragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.myacademyhomework.tinkoffmessenger.StreamListener
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.data.Stream

class StreamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val nameChannel: TextView = itemView.findViewById(R.id.textview_name_channel)
    private val imageArrow: ImageView = itemView.findViewById(R.id.imageView_arrow)

    fun onBind(stream: Stream, listener: StreamListener) {
        nameChannel.text = stream.nameChannel
        imageArrow.setOnClickListener {
            it.isSelected = !it.isSelected
            listener.itemChannelClicked(stream.topics, adapterPosition, it.isSelected)
        }
    }

    companion object {
        fun createChannelViewHolder(parent: ViewGroup): StreamViewHolder {
            return StreamViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_holder_channel, parent, false)
            )
        }
    }
}