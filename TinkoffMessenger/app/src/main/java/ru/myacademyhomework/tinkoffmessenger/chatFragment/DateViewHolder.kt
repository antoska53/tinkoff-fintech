package ru.myacademyhomework.tinkoffmessenger.chatFragment

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.myacademyhomework.tinkoffmessenger.data.DateMessage
import ru.myacademyhomework.tinkoffmessenger.data.Message


class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun onBind(date: DateMessage){
        if(itemView is TextView){
            itemView.text = date.toString()
        }
    }
}