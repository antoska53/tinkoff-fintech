package ru.myacademyhomework.tinkoffmessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.myacademyhomework.tinkoffmessenger.channelsfragment.ChannelsFragment
import ru.myacademyhomework.tinkoffmessenger.chatFragment.ChatFragment
import ru.myacademyhomework.tinkoffmessenger.peoplefragment.PeopleFragment
import ru.myacademyhomework.tinkoffmessenger.profilefragment.ProfileFragment
import ru.myacademyhomework.tinkoffmessenger.streamfragment.StreamFragment
import java.lang.IllegalArgumentException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigation?.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_channels -> {
                    loadFragment(ChannelsFragment.newInstance("", ""))
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



        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, StreamFragment.newInstance("",""))
                .commitNow()
        }


    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commitAllowingStateLoss()
    }

}