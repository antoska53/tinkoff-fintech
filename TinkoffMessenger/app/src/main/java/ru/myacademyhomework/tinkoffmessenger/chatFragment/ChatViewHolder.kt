package ru.myacademyhomework.tinkoffmessenger.chatFragment

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
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
        for (emoji in message.listEmoji){
            Log.d("EMOJI", "onBind: $adapterPosition")
            val emojiView = EmojiView(itemView.context)
            emojiView.smile = emoji
            flexBoxEmoji.addView(emojiView)
        }

        messageView.setOnLongClickListener { listener.itemLongClicked(adapterPosition) }

        Glide.with(itemView)
            .load(R.mipmap.ic_launcher)
            .into(imageviewAvatar)
    }
}