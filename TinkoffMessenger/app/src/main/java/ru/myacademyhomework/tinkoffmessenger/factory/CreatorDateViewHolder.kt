package ru.myacademyhomework.tinkoffmessenger.factory

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.chatFragment.DateViewHolder

class CreatorDateViewHolder : CreatorViewHolder() {
    override fun createViewHolder(parent: ViewGroup): DateViewHolder {
        return DateViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_holder_date, parent, false)
        )
    }
}