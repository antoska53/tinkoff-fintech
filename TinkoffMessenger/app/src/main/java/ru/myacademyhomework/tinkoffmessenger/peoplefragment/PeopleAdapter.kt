package ru.myacademyhomework.tinkoffmessenger.peoplefragment

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.myacademyhomework.tinkoffmessenger.factory.PeopleFactory

class PeopleAdapter: RecyclerView.Adapter<PeopleViewHolder>() {
    private val people = PeopleFactory.listPeople

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleViewHolder {
        return PeopleViewHolder.createPeopleViewHolder(parent)
    }

    override fun onBindViewHolder(holder: PeopleViewHolder, position: Int) {
        holder.onBind(people[position])
    }

    override fun getItemCount(): Int = people.size
}