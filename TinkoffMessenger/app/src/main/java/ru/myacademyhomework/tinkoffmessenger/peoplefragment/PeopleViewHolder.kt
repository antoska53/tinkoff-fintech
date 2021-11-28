package ru.myacademyhomework.tinkoffmessenger.peoplefragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.myacademyhomework.tinkoffmessenger.listeners.PeopleListener
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.network.User

class PeopleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val nameUser: TextView = itemView.findViewById(R.id.textview_name_users)
    private val avatarUser: ImageView = itemView.findViewById(R.id.imageview_avatar_users)
    private val emailUser: TextView = itemView.findViewById(R.id.textview_email_users)


    fun onBind(user: User, listener: PeopleListener) {
        nameUser.text = user.fullName
        emailUser.text = user.email
        itemView.setOnClickListener { listener.itemPeopleClick(user.userID) }

        Glide.with(itemView)
            .load(user.avatarURL)
            .circleCrop()
            .into(avatarUser)
    }

    companion object {
        fun createPeopleViewHolder(parent: ViewGroup): PeopleViewHolder {
            return PeopleViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_holder_user, parent, false)
            )
        }
    }
}