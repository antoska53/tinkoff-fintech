package ru.myacademyhomework.tinkoffmessenger.chatFragment.bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.listeners.BottomSheetActionListener

class BottomSheetActionAdapter(
    private val idMessage: Long,
    private val nameTopic: String,
    private val message: String,
    private val positionMessage: Int,
    private val listener: BottomSheetActionListener
) : RecyclerView.Adapter<BottomSheetActionViewHolder>() {

    private val actionList = listOf<String>(
        "Add reaction",
        "Delete message",
        "Edit message",
        "Copy message"
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetActionViewHolder {
        return BottomSheetActionViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_holder_bottom_sheet_action, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BottomSheetActionViewHolder, position: Int) {
        holder.onBind(
            nameAction = actionList[position],
            idMessage = idMessage,
            nameTopic = nameTopic,
            message = message,
            positionMessage = positionMessage,
            listener = listener)
    }

    override fun getItemCount(): Int = actionList.size
}