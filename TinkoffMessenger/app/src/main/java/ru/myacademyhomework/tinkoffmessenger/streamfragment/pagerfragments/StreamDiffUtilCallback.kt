package ru.myacademyhomework.tinkoffmessenger.streamfragment.pagerfragments

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import ru.myacademyhomework.tinkoffmessenger.data.Item
import ru.myacademyhomework.tinkoffmessenger.data.Stream
import ru.myacademyhomework.tinkoffmessenger.network.Topic

class StreamDiffUtilCallback(
    private val oldLIst: List<Item>,
    private val newLIst: List<Item>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldLIst.size

    override fun getNewListSize(): Int = newLIst.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldLIst[oldItemPosition]
        val newItem = newLIst[newItemPosition]
        if (oldItem is Stream && newItem is Topic)
            return false
        if (oldItem is Stream && newItem is Stream) {
            return oldItem.nameChannel == newItem.nameChannel
        }
        if (oldItem is Topic && newItem is Topic) {
            return oldItem.name == newItem.name
        }
        return false
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldLIst[oldItemPosition]
        val newItem = newLIst[newItemPosition]
        return if (oldItem is Stream && newItem is Stream) {
            oldItem.nameChannel == newItem.nameChannel && oldItem.isSelected == newItem.isSelected
        } else {
            val oldTopic = oldItem as Topic
            val newTopic =  newItem as Topic
            oldTopic == newTopic
        }
    }
}