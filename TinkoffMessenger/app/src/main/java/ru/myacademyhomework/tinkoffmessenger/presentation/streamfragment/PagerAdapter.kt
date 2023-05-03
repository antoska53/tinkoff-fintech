package ru.myacademyhomework.tinkoffmessenger.presentation.streamfragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.myacademyhomework.tinkoffmessenger.presentation.streamfragment.pagerfragment.AllStreamFragment
import ru.myacademyhomework.tinkoffmessenger.presentation.streamfragment.pagerfragment.SubscribedFragment

class PagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragments: List<Fragment> =
        listOf(SubscribedFragment.newInstance(), AllStreamFragment.newInstance())

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

}