package ru.myacademyhomework.tinkoffmessenger.chatFragment

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.myacademyhomework.tinkoffmessenger.ChatMessageListener
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.messageview.EmojiView
import ru.myacademyhomework.tinkoffmessenger.messageview.FlexBoxLayout
import ru.myacademyhomework.tinkoffmessenger.messageview.NameMessageView
import ru.myacademyhomework.tinkoffmessenger.network.RetrofitModule
import ru.myacademyhomework.tinkoffmessenger.network.UserMessage

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
                    val disposable =
                        RetrofitModule.chatApi.addReaction(message.id, emoji.emojiName)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                it.isSelected = !it.isSelected
                                val reactionCount = emojiView.textCount.toInt() + 1
                                emojiView.textCount = reactionCount.toString()
                            },
                                {
                                    Snackbar.make(
                                        itemView,
                                        "Неудалось добавить эмодзи \uD83D\uDE2D",
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                })
                } else {
                    val disposable =
                        RetrofitModule.chatApi.removeReaction(message.id, emoji.emojiName)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                it.isSelected = !it.isSelected
                                val reactionCount = emojiView.textCount.toInt() - 1
                                if (reactionCount == 0) {
                                    flexBoxEmoji.removeView(emojiView)
                                    if (flexBoxEmoji.childCount == 1) {
                                        flexBoxEmoji.removeAllViews()
                                    }
                                } else {
                                    emojiView.textCount = reactionCount.toString()
                                }
                            }, {
                                Snackbar.make(
                                    itemView,
                                    "Неудалось удалить эмодзи",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            })

                }
            }
            emojiView.smile = String(Character.toChars(emoji.emojiCode.toInt(16)))
            emojiView.textCount = message.reactions.count {
                it.emojiName == emoji.emojiName
            }.toString()
            flexBoxEmoji.addView(emojiView)
        }


        if (flexBoxEmoji.childCount != 0) {
            val plusButton = LayoutInflater.from(itemView.context)
                .inflate(R.layout.emoji_view_plus_layout, flexBoxEmoji, false)
            plusButton.setOnClickListener { listener.itemLongClicked(message.id, adapterPosition) }
            flexBoxEmoji.addView(plusButton)
        }

        messageView.setOnLongClickListener { listener.itemLongClicked(message.id, adapterPosition) }

        Glide.with(itemView)
            .load(message.avatarURL)
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