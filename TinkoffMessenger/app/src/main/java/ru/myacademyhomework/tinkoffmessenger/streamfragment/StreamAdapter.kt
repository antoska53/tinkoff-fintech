package ru.myacademyhomework.tinkoffmessenger.streamfragment

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.myacademyhomework.tinkoffmessenger.ChannelListener
import ru.myacademyhomework.tinkoffmessenger.data.ItemChannel
import ru.myacademyhomework.tinkoffmessenger.factory.ChannelFactory
import java.lang.IllegalArgumentException


class StreamAdapter(private val listener: ChannelListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var channels: MutableList<Any> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            AllStreamFragment.TYPE_ITEM_CHANNEL -> ChannelViewHolder.createChannelViewHolder(parent)
            AllStreamFragment.TYPE_STREAM -> StreamViewHolder.createStreamViewHolder(parent)
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ChannelViewHolder -> holder.onBind(channels[position] as ItemChannel, listener)
            is StreamViewHolder -> holder.onBind(channels[position].toString())
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int = channels.size

    override fun getItemViewType(position: Int): Int {
        return when(channels[position]){
            is ItemChannel -> AllStreamFragment.TYPE_ITEM_CHANNEL
            is String -> AllStreamFragment.TYPE_STREAM
            else -> throw IllegalArgumentException()
        }
    }

    fun updateData(position: Int, count: Int, remove: Boolean){
        if(remove) notifyItemRangeRemoved(position + 1, count)
        else notifyItemRangeInserted(position + 1, count)
    }


}