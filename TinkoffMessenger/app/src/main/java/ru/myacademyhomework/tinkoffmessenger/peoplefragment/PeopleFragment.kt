package ru.myacademyhomework.tinkoffmessenger.peoplefragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.myacademyhomework.tinkoffmessenger.R


class PeopleFragment : Fragment(R.layout.fragment_people) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = view.findViewById<RecyclerView>(R.id.recycler_view_user)
        recycler.adapter = PeopleAdapter()
    }

    companion object {

        @JvmStatic
        fun newInstance() = PeopleFragment()
    }
}