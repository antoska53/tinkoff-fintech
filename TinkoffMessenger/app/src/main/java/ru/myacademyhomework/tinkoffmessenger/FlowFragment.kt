package ru.myacademyhomework.tinkoffmessenger

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.myacademyhomework.tinkoffmessenger.peoplefragment.PeopleFragment
import ru.myacademyhomework.tinkoffmessenger.profilefragment.ProfileFragment
import ru.myacademyhomework.tinkoffmessenger.streamfragment.StreamFragment
import java.lang.IllegalArgumentException


class FlowFragment : Fragment(R.layout.fragment_flow) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadFragment(StreamFragment.newInstance())

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigation = view.findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_channels -> {
                    loadFragment(StreamFragment.newInstance())
                    true
                }
                R.id.navigation_people -> {
                    loadFragment(PeopleFragment.newInstance())
                    true
                }
                R.id.navigation_profile -> {
                    loadFragment(ProfileFragment.newInstance())
                    true
                }
                else -> throw IllegalArgumentException()
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.flow_fragment_container, fragment)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    companion object {
        const val FLOW_FRAGMENT = "FLOW_FRAGMENT"
        @JvmStatic
        fun newInstance() =
            FlowFragment()
    }
}