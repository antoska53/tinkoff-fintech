package ru.myacademyhomework.tinkoffmessenger.presentation.peoplefragment

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.myacademyhomework.tinkoffmessenger.presentation.listeners.PeopleListener
import ru.myacademyhomework.tinkoffmessenger.domain.profile.UserInfo

class PeopleAdapter(private val listener: PeopleListener) : RecyclerView.Adapter<PeopleViewHolder>() {

    val people = mutableListOf<UserInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleViewHolder {
        return PeopleViewHolder.createPeopleViewHolder(parent)
    }

    override fun onBindViewHolder(holder: PeopleViewHolder, position: Int) {
        holder.onBind(people[position], listener)
    }

    override fun getItemCount(): Int = people.size

    fun addData(users: List<UserInfo>) {
        people.clear()
        people.addAll(users)
    }
}