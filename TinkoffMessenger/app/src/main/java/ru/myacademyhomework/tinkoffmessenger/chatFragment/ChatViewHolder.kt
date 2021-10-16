package ru.myacademyhomework.tinkoffmessenger.chatFragment

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.data.Message

class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val textviewName = itemView.findViewById<TextView>(R.id.textview_name)
    private val textviewMessage = itemView.findViewById<TextView>(R.id.textview_message)
    private val imageviewAvatar = itemView.findViewById<ImageView>(R.id.imageview_avatar)

    fun onBind(message: Message){
        textviewName.text = message.name
        textviewMessage.text = message.message

        Glide.with(itemView)
            .load(R.mipmap.ic_launcher)
            .into(imageviewAvatar)
    }
}