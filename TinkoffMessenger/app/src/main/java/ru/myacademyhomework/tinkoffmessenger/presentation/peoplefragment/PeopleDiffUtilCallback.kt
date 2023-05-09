package ru.myacademyhomework.tinkoffmessenger.presentation.peoplefragment

import androidx.recyclerview.widget.DiffUtil
import ru.myacademyhomework.tinkoffmessenger.domain.profile.UserInfo

class PeopleDiffUtilCallback(
    private val oldLIst: List<UserInfo>,
    private val newLIst: List<UserInfo>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldLIst.size

    override fun getNewListSize(): Int = newLIst.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldLIst[oldItemPosition]
        val newItem = newLIst[newItemPosition]
        return oldItem.userID == newItem.userID
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldLIst[oldItemPosition]
        val newItem = newLIst[newItemPosition]
        return oldItem == newItem
    }
}