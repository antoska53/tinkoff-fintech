package ru.myacademyhomework.tinkoffmessenger.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.presentation.peoplefragment.PeopleFragment
import ru.myacademyhomework.tinkoffmessenger.presentation.profilefragment.ProfileFragment
import ru.myacademyhomework.tinkoffmessenger.presentation.streamfragment.StreamFragment
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

        setStatusBarColor(DARK_COLOR)

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

    private fun setStatusBarColor(color: Int) {
        val window = activity?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.statusBarColor = resources.getColor(color, null)
    }

    override fun showRefresh() {}

    override fun hideRefresh() {}

    override fun showError() {}


    companion object {
        const val FLOW_FRAGMENT = "FLOW_FRAGMENT"
        const val LIGHT_COLOR = R.color.status_bar_light_color
        const val DARK_COLOR = R.color.status_bar_dark_color

        @JvmStatic
        fun newInstance() = FlowFragment()
    }
}