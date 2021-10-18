package ru.myacademyhomework.tinkoffmessenger.chatFragment

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.myacademyhomework.tinkoffmessenger.ChatMessageListener
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.data.Message
import ru.myacademyhomework.tinkoffmessenger.messageview.EmojiView
import ru.myacademyhomework.tinkoffmessenger.messageview.FlexBoxLayout
import ru.myacademyhomework.tinkoffmessenger.messageview.NameMessageView

class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val textviewName = itemView.findViewById<TextView>(R.id.textview_name)
    private val textviewMessage = itemView.findViewById<TextView>(R.id.textview_message)
    private val messageView = itemView.findViewById<NameMessageView>(R.id.message_view)
    private val imageviewAvatar = itemView.findViewById<ImageView>(R.id.imageview_avatar)
    private val flexBoxEmoji = itemView.findViewById<FlexBoxLayout>(R.id.flexbox_emoji)

    fun onBind(message: Message, listener: ChatMessageListener) {

        textviewName.text = message.name
        textviewMessage.text = message.message
        flexBoxEmoji.removeAllViews()

        for (emoji in message.listEmoji) {
            val emojiView: EmojiView = LayoutInflater.from(itemView.context)
                .inflate(R.layout.emoji_view_layout, flexBoxEmoji, false) as EmojiView

            emojiView.setOnClickListener {
                emojiView.isSelected = !it.isSelected
                if (emojiView.isSelected) {
                    val reactionCount = emojiView.textCount.toInt() + 1
                    emojiView.textCount = reactionCount.toString()
                } else {
                    val reactionCount = emojiView.textCount.toInt() - 1
                    if (reactionCount == 0) {
                        flexBoxEmoji.removeView(emojiView)
                        if (flexBoxEmoji.childCount == 1) {
                            flexBoxEmoji.removeAllViews()
                        }
                        message.listEmoji.remove(emoji)
                    } else {
                        emojiView.textCount = reactionCount.toString()
                    }
                }
            }

            emojiView.smile = emoji
            emojiView.isSelected = true
            emojiView.textCount = "1"

            flexBoxEmoji.addView(emojiView)
        }

        if (flexBoxEmoji.childCount != 0) {
            val plusButton = LayoutInflater.from(itemView.context)
                .inflate(R.layout.emoji_view_plus_layout, flexBoxEmoji, false)
            plusButton.setOnClickListener { listener.itemLongClicked(adapterPosition) }
            flexBoxEmoji.addView(plusButton)
        }

        messageView.setOnLongClickListener { listener.itemLongClicked(adapterPosition) }

        Glide.with(itemView)
            .load(R.mipmap.ic_launcher)
            .into(imageviewAvatar)
    }

}