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
                .add(
                    R.id.fragment_container, fragment,
                    FlowFragment.FLOW_FRAGMENT
                )
//                .setPrimaryNavigationFragment(fragment)
                .commit()
        }
    }

    override fun changeFlowFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            //            .setPrimaryNavigationFragment(fragment)
            .commitAllowingStateLoss()

    }

    override fun changeBottomNavFragment(fragment: Fragment, toBackstack: Boolean) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flow_fragment_container, fragment)
            if (toBackstack) addToBackStack(null)
            commitAllowingStateLoss()
        }
    }
}