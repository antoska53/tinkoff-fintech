package ru.myacademyhomework.tinkoffmessenger.peoplefragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import ru.myacademyhomework.tinkoffmessenger.App
import ru.myacademyhomework.tinkoffmessenger.FragmentNavigation
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.network.User
import ru.myacademyhomework.tinkoffmessenger.profilefragment.ProfileFragment
import javax.inject.Inject
import javax.inject.Provider


class PeopleFragment : MvpAppCompatFragment(R.layout.fragment_people), PeopleView {

    private var adapter: PeopleAdapter? = null
    private var recycler: RecyclerView? = null
    private var navigation: FragmentNavigation? = null
    private var shimmer: ShimmerFrameLayout? = null
    private var errorView: View? = null
    private var editTextSearch: EditText? = null

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
        (activity?.application as App).appComponent.getPeopleComponent().inject(this)
        super.onCreate(savedInstanceState)
        peoplePresenter.initSearch()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shimmer = view.findViewById(R.id.shimmer_people_layout)
        errorView = view.findViewById(R.id.error_view)
        val buttonReload = view.findViewById<Button>(R.id.button_reload)
        buttonReload.setOnClickListener {
            peoplePresenter.buttonReloadClick()
        }
        recycler = view.findViewById(R.id.recycler_view_user)
        adapter = PeopleAdapter { userId ->
            peoplePresenter.openProfile(userId)
        }
        recycler?.adapter = adapter

        editTextSearch = view.findViewById(R.id.search_user_edittext)
        editTextSearch?.addTextChangedListener { str ->
            peoplePresenter.search(str.toString())
        }

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

    override fun showRefresh() {
        shimmer?.isVisible = true
        shimmer?.startShimmer()
        recycler?.isVisible = false
        errorView?.isVisible = false
    }

    override fun hideRefresh() {
        recycler?.isVisible = true
        shimmer?.isVisible = false
        shimmer?.stopShimmer()
    }

    override fun showError() {
        recycler?.isVisible = false
        errorView?.isVisible = true
    }

    override fun showErrorUpdateData() {
        Snackbar.make(
            requireView(),
            "Неудалось обновить данные",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun showIsEmptyResultSearch() {
        Snackbar.make(
            requireView(),
            "Ничего не найдено",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun showUsers() {
        peoplePresenter.getAllUsersFromDb()
    }

    override fun showRecycler() {
        recycler?.isVisible = true
        errorView?.isVisible = false
    }

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