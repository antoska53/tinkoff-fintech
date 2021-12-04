package ru.myacademyhomework.tinkoffmessenger.streamfragment.pagerfragments

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.myacademyhomework.tinkoffmessenger.StreamListener
import ru.myacademyhomework.tinkoffmessenger.TopicListener
import ru.myacademyhomework.tinkoffmessenger.data.Item
import ru.myacademyhomework.tinkoffmessenger.data.Stream
import ru.myacademyhomework.tinkoffmessenger.network.Topic
import java.lang.IllegalArgumentException


class StreamAdapter(
    private val streamListener: StreamListener,
    private val topicListener: TopicListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var streams: MutableList<Item> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            AllStreamFragment.TYPE_STREAM -> StreamViewHolder.createChannelViewHolder(parent)
            AllStreamFragment.TYPE_TOPIC -> TopicViewHolder.createStreamViewHolder(parent)
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is StreamViewHolder -> holder.onBind(
                streams[position] as Stream,
                streamListener
            )
            is TopicViewHolder -> holder.onBind(streams[position] as Topic, topicListener)
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int = streams.size

    override fun getItemViewType(position: Int): Int {
        return when (streams[position]) {
            is Stream -> AllStreamFragment.TYPE_STREAM
            is Topic -> AllStreamFragment.TYPE_TOPIC
            else -> throw IllegalArgumentException()
        }
    }

    fun updateData(streams: List<Item>, position: Int, remove: Boolean) {
        if (remove) {
            this.streams.removeAll(streams)
            notifyItemRangeRemoved(position + 1, streams.size)
        } else {
            this.streams.addAll(position + 1, streams)
            notifyItemRangeInserted(position + 1, streams.size)
        }
    }

    fun setData(list: List<Item>) {
        streams.clear()
        streams.addAll(list)
    }
}