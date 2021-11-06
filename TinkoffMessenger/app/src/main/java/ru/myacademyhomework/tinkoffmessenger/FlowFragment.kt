package ru.myacademyhomework.tinkoffmessenger

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.myacademyhomework.tinkoffmessenger.peoplefragment.PeopleFragment
import ru.myacademyhomework.tinkoffmessenger.profilefragment.ProfileFragment
import ru.myacademyhomework.tinkoffmessenger.streamfragment.StreamFragment
import java.lang.IllegalArgumentException


class FlowFragment : Fragment(R.layout.fragment_flow) {
    private var navigation: FragmentNavigation? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentNavigation) {
            navigation = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement FragmentNavigation")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadFragment(StreamFragment.newInstance(), false)

        val bottomNavigation = view.findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_channels -> {
                    loadFragment(StreamFragment.newInstance(), true)
                    true
                }
                R.id.navigation_people -> {
                    loadFragment(PeopleFragment.newInstance(), true)
                    true
                }
                R.id.navigation_profile -> {
                    loadFragment(ProfileFragment.newInstance(), true)
                    true
                }
                else -> throw IllegalArgumentException()
            }
        }
    }

    private fun loadFragment(fragment: Fragment, toBackstack: Boolean) {
        navigation?.changeFragment(fragment, toBackstack)
    }


    companion object {
        const val FLOW_FRAGMENT = "FLOW_FRAGMENT"

        @JvmStatic
        fun newInstance() = FlowFragment()
    }
}