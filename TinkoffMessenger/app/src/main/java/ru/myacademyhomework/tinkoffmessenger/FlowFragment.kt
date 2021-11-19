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
        require(context is FragmentNavigation)
        navigation = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigation = view.findViewById<BottomNavigationView>(R.id.nav_view)
        val itemId = bottomNavigation.selectedItemId
        loadFragment(itemId, false)

        bottomNavigation.setOnItemSelectedListener {
            if (it.itemId == bottomNavigation.selectedItemId) {
                false
            } else {
                loadFragment(it.itemId, false)
                true
            }
        }
    }

    private fun loadFragment(itemId: Int, toBackstack: Boolean) {
        when (itemId) {
            R.id.navigation_channels -> {
                navigation?.changeFragment(StreamFragment.newInstance(), toBackstack)
            }
            R.id.navigation_people -> {
                navigation?.changeFragment(PeopleFragment.newInstance(), toBackstack)
            }
            R.id.navigation_profile -> {
                navigation?.changeFragment(ProfileFragment.newInstance(0), toBackstack)
            }
            else -> throw IllegalArgumentException()
        }
    }


    companion object {
        const val FLOW_FRAGMENT = "FLOW_FRAGMENT"

        @JvmStatic
        fun newInstance() = FlowFragment()
    }
}