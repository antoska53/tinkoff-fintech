package ru.myacademyhomework.tinkoffmessenger.chatFragment

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.data.DateMessage


class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun onBind(date: DateMessage) {
        itemView.findViewById<TextView>(R.id.textview_date).text = "${date.day} ${date.month}"
    }
}