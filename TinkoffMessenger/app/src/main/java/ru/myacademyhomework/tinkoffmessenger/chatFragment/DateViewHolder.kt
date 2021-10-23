package ru.myacademyhomework.tinkoffmessenger.chatFragment

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.myacademyhomework.tinkoffmessenger.R
import java.time.LocalDate


class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun onBind(date: LocalDate) {
        itemView.findViewById<TextView>(R.id.textview_date).text = "${date.dayOfMonth} ${date.month}"
    }
}