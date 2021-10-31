package ru.myacademyhomework.tinkoffmessenger.streamfragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.myacademyhomework.tinkoffmessenger.ChannelListener
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.data.ItemChannel

class ChannelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val nameChannel: TextView = itemView.findViewById(R.id.textview_name_channel)
    private val imageArrow: ImageView = itemView.findViewById(R.id.imageView_arrow)

    fun onBind(channel: ItemChannel, listener: ChannelListener){
        nameChannel.text = channel.nameChannel
        imageArrow.setOnClickListener {
            it.isSelected = !it.isSelected
            listener.itemChannelClicked(channel.streams, adapterPosition, it.isSelected)
        }
    }

    companion object{
        fun createChannelViewHolder(parent: ViewGroup): ChannelViewHolder {
            return ChannelViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_holder_channel, parent, false)
            )
        }
    }
}