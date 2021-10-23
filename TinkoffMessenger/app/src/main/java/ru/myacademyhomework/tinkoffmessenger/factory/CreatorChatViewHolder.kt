package ru.myacademyhomework.tinkoffmessenger.factory

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.chatFragment.ChatViewHolder

class CreatorChatViewHolder : CreatorViewHolder() {
    override fun createViewHolder(parent: ViewGroup): ChatViewHolder {
        return ChatViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_holder_message, parent, false)
        )
    }
}