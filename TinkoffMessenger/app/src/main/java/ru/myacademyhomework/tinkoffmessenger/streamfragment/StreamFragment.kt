package ru.myacademyhomework.tinkoffmessenger.streamfragment

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import ru.myacademyhomework.tinkoffmessenger.App
import ru.myacademyhomework.tinkoffmessenger.FlowFragment
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.database.StreamDb
import javax.inject.Inject
import javax.inject.Provider


class StreamFragment : MvpAppCompatFragment(R.layout.fragment_stream), StreamView {
    private var showStreams = true
    private var editTextSearch: EditText? = null
    private var viewPager: ViewPager2? = null
    private var pagerAdapter: PagerAdapter? = null

    @Inject
    lateinit var providePresenter: Provider<StreamPresenter>
    private val streamPresenter: StreamPresenter by moxyPresenter {
        providePresenter.get()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        (activity?.application as App).appComponent.getStreamComponent().inject(this)
        super.onCreate(savedInstanceState)

        pagerAdapter = PagerAdapter(childFragmentManager, lifecycle)

        setStatusBarColor(FlowFragment.DARK_COLOR)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabs: List<String> = listOf("Subscribed", "All streams")

        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)

        viewPager = view.findViewById(R.id.viewpager_stream)
        viewPager?.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, viewPager!!) { tab, position ->
            tab.text = tabs[position]
        }.attach()

        editTextSearch = view.findViewById(R.id.search_edittext)
        editTextSearch?.addTextChangedListener { str ->
            streamPresenter.search(str.toString())
        }

        streamPresenter.initSearch()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        streamPresenter.resetSearchFlag()
    }

    private fun setStatusBarColor(color: Int) {
        val window = activity?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.statusBarColor = resources.getColor(color, null)
    }

    override fun showStreams() {
        editTextSearch?.text?.clear()
        childFragmentManager.setFragmentResult(
            SUBSCRIBE_RESULT_KEY,
            bundleOf(SHOW_STREAMS_KEY to showStreams)
        )
    }

    override fun showResultSearch(stream: StreamDb) {
        Snackbar.make(requireView(), stream.nameChannel, Snackbar.LENGTH_SHORT).show()

        viewPager?.currentItem = 0
        childFragmentManager.setFragmentResult(
            SUBSCRIBE_RESULT_KEY,
            bundleOf(
                TOPIC_KEY to "",
                STREAM_KEY to stream.nameChannel
            )
        )
    }

    override fun showIsEmptyResultSearch() {
        Snackbar.make(requireView(), getString(R.string.empty_search), Snackbar.LENGTH_SHORT).show()
    }

    override fun showRefresh() {}

    override fun hideRefresh() {}

    override fun showError() {
        Snackbar.make(requireView(), getString(R.string.error), Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        const val SUBSCRIBE_RESULT_KEY = "SUBSCRIBE_RESULT_KEY"
        const val TOPIC_KEY = "TOPIC_KEY"
        const val STREAM_KEY = "STREAM_KEY"
        const val SHOW_STREAMS_KEY = "SHOW_STREAMS_KEY"

        @JvmStatic
        fun newInstance() = StreamFragment()
    }
}