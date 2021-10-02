package ru.myacademyhomework.homework_1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ContactAdapter : RecyclerView.Adapter<ContactViewHolder>() {
    private val contactList = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.view_holder_contact, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.onBind(contactList[position])
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    fun updateContacts(contacts: List<String>){
        contactList.clear()
        contactList.addAll(contacts)
        notifyDataSetChanged()
    }
}