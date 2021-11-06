package ru.myacademyhomework.tinkoffmessenger.streamfragment

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.myacademyhomework.tinkoffmessenger.ChannelListener
import ru.myacademyhomework.tinkoffmessenger.StreamListener
import ru.myacademyhomework.tinkoffmessenger.data.Item
import ru.myacademyhomework.tinkoffmessenger.data.ItemChannel
import ru.myacademyhomework.tinkoffmessenger.data.ItemStream
import java.lang.IllegalArgumentException


class StreamAdapter(
    private val channelListener: ChannelListener,
    private val streamListener: StreamListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val channels: MutableList<Item> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            AllStreamFragment.TYPE_ITEM_CHANNEL -> ChannelViewHolder.createChannelViewHolder(parent)
            AllStreamFragment.TYPE_STREAM -> StreamViewHolder.createStreamViewHolder(parent)
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ChannelViewHolder -> holder.onBind(
                channels[position] as ItemChannel,
                channelListener
            )
            is StreamViewHolder -> holder.onBind(channels[position] as ItemStream, streamListener)
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int = channels.size

    override fun getItemViewType(position: Int): Int {
        return when (channels[position]) {
            is ItemChannel -> AllStreamFragment.TYPE_ITEM_CHANNEL
            is ItemStream -> AllStreamFragment.TYPE_STREAM
            else -> throw IllegalArgumentException()
        }
    }

    fun updateData(streams: List<Item>, position: Int, remove: Boolean) {
        if (remove){
            channels.removeAll(streams)
            notifyItemRangeRemoved(position + 1, streams.size)
        }
        else{
            channels.addAll(position + 1, streams)
            notifyItemRangeInserted(position + 1, streams.size)
        }
    }

    fun setData(list: List<Item>){
        channels.clear()
        channels.addAll(list)
        notifyItemRangeInserted(0, channels.size)
    }
}