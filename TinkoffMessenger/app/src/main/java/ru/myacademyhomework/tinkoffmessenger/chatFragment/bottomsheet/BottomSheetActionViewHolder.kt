package ru.myacademyhomework.tinkoffmessenger.chatFragment.bottomsheet

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.data.ActionBottomSheet
import ru.myacademyhomework.tinkoffmessenger.listeners.BottomSheetActionListener

class BottomSheetActionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val textviewAction = itemView.findViewById<TextView>(R.id.textview_action_bottomsheet)

    fun onBind(
        action: ActionBottomSheet,
        idMessage: Long,
        nameTopic: String,
        message: String,
        positionMessage: Int,
        listener: BottomSheetActionListener
    ) {
        textviewAction.text = action.action
        textviewAction.setOnClickListener {
            listener.itemActionClicked(action, idMessage, nameTopic, message, positionMessage)
        }
    }
}