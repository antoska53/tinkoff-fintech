package ru.myacademyhomework.tinkoffmessenger.chatFragment

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.myacademyhomework.tinkoffmessenger.BottomSheetListener
import ru.myacademyhomework.tinkoffmessenger.R

class BottomSheetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val emojiTextView = itemView.findViewById<TextView>(R.id.emoji_bottomsheet)

    fun onBind(emoji: String, idMessage: Long, positionMessage: Int, listener: BottomSheetListener) {
        emojiTextView.text = emoji
        emojiTextView.setOnClickListener {
            listener.itemEmojiClicked(emoji = emoji, idMessage = idMessage, position = positionMessage)
        }
    }
}