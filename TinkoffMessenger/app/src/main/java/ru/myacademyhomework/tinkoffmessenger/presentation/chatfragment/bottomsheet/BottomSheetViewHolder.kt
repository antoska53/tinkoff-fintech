package ru.myacademyhomework.tinkoffmessenger.presentation.chatfragment.bottomsheet

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.myacademyhomework.tinkoffmessenger.presentation.listeners.BottomSheetListener
import ru.myacademyhomework.tinkoffmessenger.R

class BottomSheetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val emojiTextView = itemView.findViewById<TextView>(R.id.emoji_bottomsheet)

    fun onBind(emoji: String, idMessage: Long, nameTopic: String, positionMessage: Int, listener: BottomSheetListener) {
        emojiTextView.text = emoji
        emojiTextView.setOnClickListener {
            listener.itemEmojiClicked(emoji = emoji, idMessage = idMessage, nameTopic = nameTopic, position = positionMessage)
        }
    }
}