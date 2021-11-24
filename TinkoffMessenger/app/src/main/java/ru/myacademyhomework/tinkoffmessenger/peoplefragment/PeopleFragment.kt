package ru.myacademyhomework.tinkoffmessenger.peoplefragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import ru.myacademyhomework.tinkoffmessenger.database.ChatDatabase
import ru.myacademyhomework.tinkoffmessenger.FragmentNavigation
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.network.User
import ru.myacademyhomework.tinkoffmessenger.profilefragment.ProfileFragment


class PeopleFragment : MvpAppCompatFragment(R.layout.fragment_people), PeopleView {

    private var adapter: PeopleAdapter? = null
    private var recycler: RecyclerView? = null
    private var navigation: FragmentNavigation? = null
    private val peoplePresenter by moxyPresenter {
        val chatDao = ChatDatabase.getDatabase(requireContext()).chatDao()
        PeoplePresenter(chatDao)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        require(context is FragmentNavigation)
        navigation = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler = view.findViewById(R.id.recycler_view_user)
        adapter = PeopleAdapter { userId ->
            openProfileFragment(userId)
        }
        recycler?.adapter = adapter

        peoplePresenter.getAllUsersFromDb()
        peoplePresenter.getAllUsers()
    }


    override fun updateRecycler(users: List<User>) {
        adapter?.addData(users)
    }

    override fun showRefresh() {
        TODO("Not yet implemented")
    }

    override fun hideRefresh() {
        TODO("Not yet implemented")
    }

    override fun showError() {
        TODO("Not yet implemented")
    }

    private fun openProfileFragment(userId: Int) {
        navigation?.openChatFragment(ProfileFragment.newInstance(userId))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recycler?.adapter = null
    }


    companion object {

        @JvmStatic
        fun newInstance() = PeopleFragment()
    }
}