package ru.myacademyhomework.tinkoffmessenger.peoplefragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.myacademyhomework.tinkoffmessenger.FragmentNavigation
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.network.RetrofitModule
import ru.myacademyhomework.tinkoffmessenger.network.User
import ru.myacademyhomework.tinkoffmessenger.profilefragment.ProfileFragment


class PeopleFragment : Fragment(R.layout.fragment_people) {
    private var adapter: PeopleAdapter? = null
    private var recycler: RecyclerView? = null
    private var navigation: FragmentNavigation? = null
    private val compositeDisposable = CompositeDisposable()

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

        getAllUsers()
    }

    private fun getAllUsers() {
        val disposable =
            RetrofitModule.chatApi.getAllUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    updateRecycler(it.users)
                }, {

                })
        compositeDisposable.add(disposable)
    }

    private fun updateRecycler(users: List<User>) {
        adapter?.addData(users)
    }

    private fun openProfileFragment(userId: Int) {
        navigation?.openChatFragment(ProfileFragment.newInstance(userId))
    }

    override fun onDestroy() {
        super.onDestroy()
        recycler?.adapter = null
        compositeDisposable.clear()
    }

    companion object {

        @JvmStatic
        fun newInstance() = PeopleFragment()
    }
}