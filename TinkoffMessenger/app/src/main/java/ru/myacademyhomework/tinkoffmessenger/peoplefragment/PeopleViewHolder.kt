package ru.myacademyhomework.tinkoffmessenger.peoplefragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.myacademyhomework.tinkoffmessenger.R

class PeopleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val nameUser: TextView = itemView.findViewById(R.id.textview_name_users)


    fun onBind(name: String){
        nameUser.text = name
    }

    companion object{
        fun createPeopleViewHolder(parent: ViewGroup): PeopleViewHolder {
            return PeopleViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_holder_user, parent, false)
            )
        }
    }
}