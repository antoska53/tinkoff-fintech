package ru.myacademyhomework.tinkoffmessenger.streamfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ru.myacademyhomework.tinkoffmessenger.R



class StreamFragment : Fragment(R.layout.fragment_stream) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabs: List<String> = listOf("Subscribed", "All streams")
        val pagerAdapter = PagerAdapter(parentFragmentManager, lifecycle)
        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)

        val viewPager = view.findViewById<ViewPager2>(R.id.viewpager_stream)
        viewPager.adapter = pagerAdapter
        pagerAdapter.update(listOf(SubscribedFragment(), AllStreamFragment()))

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()


    }


    companion object {

        @JvmStatic
        fun newInstance() =
            StreamFragment()
    }
}