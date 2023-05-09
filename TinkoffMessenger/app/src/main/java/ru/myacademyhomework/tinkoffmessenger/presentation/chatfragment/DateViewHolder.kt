package ru.myacademyhomework.tinkoffmessenger.presentation.chatfragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.domain.chat.DateMessage


class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun onBind(date: DateMessage) {
        itemView.findViewById<TextView>(R.id.textview_date).text =
            "${date.date.dayOfMonth} ${date.date.month}"
    }

    companion object {
        fun createViewHolder(parent: ViewGroup): DateViewHolder {
            return DateViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_holder_date, parent, false)
            )
        }
    }
}