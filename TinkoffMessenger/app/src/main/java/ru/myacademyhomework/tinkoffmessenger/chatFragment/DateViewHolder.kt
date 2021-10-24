package ru.myacademyhomework.tinkoffmessenger.chatFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.myacademyhomework.tinkoffmessenger.R
import java.time.LocalDate


class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun onBind(date: LocalDate) {
        itemView.findViewById<TextView>(R.id.textview_date).text =
            "${date.dayOfMonth} ${date.month}"
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