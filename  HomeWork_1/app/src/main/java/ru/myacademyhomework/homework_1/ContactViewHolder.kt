package ru.myacademyhomework.homework_1

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvContactName: TextView = itemView.findViewById(R.id.tv_contact_name)

    fun onBind(contactName: String) {
        tvContactName.text = contactName
    }
}