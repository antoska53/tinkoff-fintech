package ru.myacademyhomework.tinkoffmessenger.peoplefragment

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.myacademyhomework.tinkoffmessenger.PeopleListener
import ru.myacademyhomework.tinkoffmessenger.network.User

class PeopleAdapter(private val listener: PeopleListener) :
    RecyclerView.Adapter<PeopleViewHolder>() {
    private val people = mutableListOf<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleViewHolder {
        return PeopleViewHolder.createPeopleViewHolder(parent)
    }

    override fun onBindViewHolder(holder: PeopleViewHolder, position: Int) {
        holder.onBind(people[position], listener)
    }

    override fun getItemCount(): Int = people.size

    fun addData(users: List<User>) {
        people.clear()
        people.addAll(users)
        notifyItemRangeInserted(0, users.size)
    }
}