package ru.myacademyhomework.tinkoffmessenger.presentation.chatfragment

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.myacademyhomework.tinkoffmessenger.presentation.listeners.ChatMessageListener
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.data.UserMessage
import ru.myacademyhomework.tinkoffmessenger.presentation.messageview.EmojiView
import ru.myacademyhomework.tinkoffmessenger.presentation.messageview.FlexBoxLayout
import ru.myacademyhomework.tinkoffmessenger.presentation.messageview.NameMessageView

class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val textviewName = itemView.findViewById<TextView>(R.id.textview_name)
    private val textviewMessage = itemView.findViewById<TextView>(R.id.textview_message)
    private val messageView = itemView.findViewById<NameMessageView>(R.id.message_view)
    private val imageviewAvatar = itemView.findViewById<ImageView>(R.id.imageview_avatar)
    private val flexBoxEmoji = itemView.findViewById<FlexBoxLayout>(R.id.flexbox_emoji)

    fun onBind(message: UserMessage, userId: Int, listener: ChatMessageListener) {

        textviewName.text = message.senderFullName
        textviewMessage.text =
            Html.fromHtml(message.content, Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH).trim()
        flexBoxEmoji.removeAllViews()

        val myReaction = message.reactions.filter {
            it.userId == userId
        }
        val reactionsDistinct = message.reactions.distinctBy { it.emojiName }
        for (emoji in reactionsDistinct) {

            val emojiView: EmojiView = LayoutInflater.from(itemView.context)
                .inflate(R.layout.emoji_view_layout, flexBoxEmoji, false) as EmojiView

            val isMyReaction = myReaction.count {
                it.emojiName == emoji.emojiName
            }
            emojiView.isSelected = isMyReaction > 0

            emojiView.setOnClickListener {
                if (!it.isSelected) {
                    listener.itemAddReactionClicked(message.id, message.nameTopic, emoji.emojiName, adapterPosition)
                } else {
                    listener.itemRemoveReactionClicked(message.id, message.nameTopic, emoji.emojiName, emoji.emojiCode, emoji.reactionType, emoji.userId, adapterPosition)
                }
            }
            emojiView.smile = emoji.emojiCode.split("-").joinToString("") {
                if (it == "zulip") {
                    "Z "
                } else {
                    String(Character.toChars(it.toInt(16)))
                }
            }
            emojiView.textCount = message.reactions.count {
                it.emojiName == emoji.emojiName
            }.toString()
            flexBoxEmoji.addView(emojiView)
        }


        if (flexBoxEmoji.childCount != 0) {
            val plusButton = LayoutInflater.from(itemView.context)
                .inflate(R.layout.emoji_view_plus_layout, flexBoxEmoji, false)
            plusButton.setOnClickListener { listener.plusButtonClicked(message.id, message.nameTopic, adapterPosition) }
            flexBoxEmoji.addView(plusButton)
        }

        messageView.setOnLongClickListener { listener.itemLongClicked(message.id, message.nameTopic, message.content, adapterPosition) }

        Glide.with(itemView)
            .load(message.avatarURL)
            .circleCrop()
            .into(imageviewAvatar)
    }

    companion object {
        fun createViewHolder(parent: ViewGroup): ChatViewHolder {
            return ChatViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_holder_message, parent, false)
            )
        }
    }
}