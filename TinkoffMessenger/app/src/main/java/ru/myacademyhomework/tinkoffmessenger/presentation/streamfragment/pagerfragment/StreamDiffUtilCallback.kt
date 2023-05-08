package ru.myacademyhomework.tinkoffmessenger.presentation.streamfragment.pagerfragment

import androidx.recyclerview.widget.DiffUtil
import ru.myacademyhomework.tinkoffmessenger.data.Item
import ru.myacademyhomework.tinkoffmessenger.data.Stream
import ru.myacademyhomework.tinkoffmessenger.data.network.model.TopicDto

class StreamDiffUtilCallback(
    private val oldLIst: List<Item>,
    private val newLIst: List<Item>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldLIst.size

    override fun getNewListSize(): Int = newLIst.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldLIst[oldItemPosition]
        val newItem = newLIst[newItemPosition]
        if (oldItem is Stream && newItem is TopicDto)
            return false
        if (oldItem is Stream && newItem is Stream) {
            return oldItem.nameChannel == newItem.nameChannel
        }
        if (oldItem is TopicDto && newItem is TopicDto) {
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
            val oldTopic = oldItem as TopicDto
            val newTopic =  newItem as TopicDto
            oldTopic == newTopic
        }
    }
}