package ru.myacademyhomework.tinkoffmessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import ru.myacademyhomework.tinkoffmessenger.chatFragment.ChatFragment

class MainActivity : AppCompatActivity(), FragmentNavigation {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val fragment = FlowFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment,
                    FlowFragment.FLOW_FRAGMENT
                )
//                .setPrimaryNavigationFragment(fragment)
                .commitNow()
        }
    }

    override fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, fragment)
            .addToBackStack(null)
//            .setPrimaryNavigationFragment(fragment)
            .commitAllowingStateLoss()
    }
}