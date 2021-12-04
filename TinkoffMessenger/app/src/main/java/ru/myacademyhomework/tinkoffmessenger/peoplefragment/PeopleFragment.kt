package ru.myacademyhomework.tinkoffmessenger.peoplefragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import ru.myacademyhomework.tinkoffmessenger.AppDelegate
import ru.myacademyhomework.tinkoffmessenger.FragmentNavigation
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.di.people.PeopleModule
import ru.myacademyhomework.tinkoffmessenger.network.User
import ru.myacademyhomework.tinkoffmessenger.profilefragment.ProfileFragment
import javax.inject.Inject
import javax.inject.Provider


class PeopleFragment : MvpAppCompatFragment(R.layout.fragment_people), PeopleView {

    private var adapter: PeopleAdapter? = null
    private var recycler: RecyclerView? = null
    private var navigation: FragmentNavigation? = null

    @Inject
    lateinit var presenterProvider: Provider<PeoplePresenter>
    private val peoplePresenter by moxyPresenter {
        presenterProvider.get()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        require(context is FragmentNavigation)
        navigation = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppDelegate.appComponent.getPeopleComponent(PeopleModule()).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler = view.findViewById(R.id.recycler_view_user)
        adapter = PeopleAdapter { userId ->
            peoplePresenter.openProfile(userId)
        }
        recycler?.adapter = adapter

        peoplePresenter.getAllUsersFromDb()
        peoplePresenter.getAllUsers()
    }


    override fun updateRecycler(users: List<User>) {
        adapter?.let {
            val peopleDiffUtilCallback = PeopleDiffUtilCallback(it.people, users)
            val peopleDiffResult = DiffUtil.calculateDiff(peopleDiffUtilCallback)
            it.addData(users)
            peopleDiffResult.dispatchUpdatesTo(it)
        }
    }

    override fun showRefresh() {}

    override fun hideRefresh() {}

    override fun showError() {}

    override fun openProfileFragment(userId: Int) {
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