package ru.myacademyhomework.tinkoffmessenger.chatFragment.bottomsheet

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.listeners.BottomSheetActionListener

class BottomSheetActionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val textviewAction = itemView.findViewById<TextView>(R.id.textview_action_bottomsheet)

    fun onBind(
        nameAction: String,
        idMessage: Long,
        nameTopic: String,
        message: String,
        positionMessage: Int,
        listener: BottomSheetActionListener
    ) {
        textviewAction.text = nameAction
        textviewAction.setOnClickListener {
            listener.itemActionClicked(nameAction, idMessage, nameTopic, message, positionMessage)
        }
    }
}