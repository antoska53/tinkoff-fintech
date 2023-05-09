package ru.myacademyhomework.tinkoffmessenger.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import ru.myacademyhomework.tinkoffmessenger.R

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
                .commit()
        }
    }

    override fun openChatFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commitAllowingStateLoss()

    }

    override fun changeFragment(fragment: Fragment, toBackstack: Boolean) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flow_fragment_container, fragment)
            if (toBackstack) addToBackStack(null)
            commitAllowingStateLoss()
        }
    }

    override fun changeFlowFragment(fragment: Fragment, toBackstack: Boolean) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            if (toBackstack) addToBackStack(null)
            commitAllowingStateLoss()
        }
    }
}