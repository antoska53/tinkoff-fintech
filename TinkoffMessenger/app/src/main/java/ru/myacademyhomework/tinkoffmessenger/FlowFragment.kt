package ru.myacademyhomework.tinkoffmessenger

import android.content.Context
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import ru.myacademyhomework.tinkoffmessenger.peoplefragment.PeopleFragment
import ru.myacademyhomework.tinkoffmessenger.profilefragment.ProfileFragment
import ru.myacademyhomework.tinkoffmessenger.streamfragment.StreamFragment
import java.lang.IllegalArgumentException


class FlowFragment : MvpAppCompatFragment(R.layout.fragment_flow), FlowFragmentView {
    private var navigation: FragmentNavigation? = null
    private val flowFragmentPresenter by moxyPresenter {
        FlowFragmentPresenter()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        require(context is FragmentNavigation)
        navigation = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            flowFragmentPresenter.loadFragment(R.id.navigation_channels, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigation = view.findViewById<BottomNavigationView>(R.id.nav_view)

        bottomNavigation.setOnItemSelectedListener {
            if (it.itemId == bottomNavigation.selectedItemId) {
                false
            } else {
                flowFragmentPresenter.loadFragment(it.itemId, false)
                true
            }
        }
    }

    override fun loadFragment(itemId: Int, toBackstack: Boolean) {
        when (itemId) {
            R.id.navigation_channels -> {
                navigation?.changeFragment(StreamFragment.newInstance(), toBackstack)
            }
            R.id.navigation_people -> {
                navigation?.changeFragment(PeopleFragment.newInstance(), toBackstack)
            }
            R.id.navigation_profile -> {
                navigation?.changeFragment(
                    ProfileFragment.newInstance(ProfileFragment.USER_OWNER),
                    toBackstack
                )
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun showRefresh() {}

    override fun hideRefresh() {}

    override fun showError() {}


    companion object {
        const val FLOW_FRAGMENT = "FLOW_FRAGMENT"

        @JvmStatic
        fun newInstance() = FlowFragment()
    }
}