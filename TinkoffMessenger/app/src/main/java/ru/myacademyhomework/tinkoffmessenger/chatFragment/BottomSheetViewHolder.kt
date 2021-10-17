package ru.myacademyhomework.tinkoffmessenger.chatFragment

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.myacademyhomework.tinkoffmessenger.BottomSheetListener
import ru.myacademyhomework.tinkoffmessenger.R

class BottomSheetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val emojiTextView = itemView.findViewById<TextView>(R.id.emoji_bottomsheet)

    fun onBind(emoji: String, listener: BottomSheetListener){
        emojiTextView.text = emoji
        emojiTextView.setOnClickListener {
                listener.itemEmojiClicked(emoji)
//            val flexBox = recyclerView.findViewById<FlexBoxLayout>(R.id.flexbox_emoji)
//            val emojiView = EmojiView(itemView.context)
//            emojiView.smile = emoji
//            flexBox.addView(emojiView)
        }
    }
}