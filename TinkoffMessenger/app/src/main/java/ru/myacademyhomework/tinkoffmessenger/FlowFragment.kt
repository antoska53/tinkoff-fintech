package ru.myacademyhomework.tinkoffmessenger

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.myacademyhomework.tinkoffmessenger.peoplefragment.PeopleFragment
import ru.myacademyhomework.tinkoffmessenger.profilefragment.ProfileFragment
import ru.myacademyhomework.tinkoffmessenger.streamfragment.StreamFragment
import java.lang.IllegalArgumentException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FlowFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FlowFragment : Fragment(R.layout.fragment_flow) {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        loadFragment(StreamFragment.newInstance("",""))

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigation = view.findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_channels -> {
                    loadFragment(StreamFragment.newInstance("", ""))
                    true
                }
                R.id.navigation_people -> {
                    loadFragment(PeopleFragment.newInstance("", ""))
                    true
                }
                R.id.navigation_profile -> {
                    loadFragment(ProfileFragment.newInstance("", ""))
                    true
                }
                else -> throw IllegalArgumentException()
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.flow_fragment_container, fragment)
            .commitAllowingStateLoss()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FlowFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FlowFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}