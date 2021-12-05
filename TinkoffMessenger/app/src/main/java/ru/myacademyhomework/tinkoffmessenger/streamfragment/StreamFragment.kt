package ru.myacademyhomework.tinkoffmessenger.streamfragment

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import ru.myacademyhomework.tinkoffmessenger.App
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.network.Topic
import javax.inject.Inject
import javax.inject.Provider


class StreamFragment : MvpAppCompatFragment(R.layout.fragment_stream), StreamView {
    private var showStreams = true
    private var editTextSearch: EditText? = null
    private var viewPager: ViewPager2? = null

    @Inject
    lateinit var providePresenter: Provider<StreamPresenter>
    private var onBackPressedCallback: OnBackPressedCallback? = null
    private val streamPresenter: StreamPresenter by moxyPresenter {
        providePresenter.get()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        (activity?.application as App).appComponent.getStreamComponent().inject(this)
        super.onCreate(savedInstanceState)

        onBackPressedCallback = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                streamPresenter.backPressed()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabs: List<String> = listOf("Subscribed", "All streams")
        val pagerAdapter = PagerAdapter(childFragmentManager, lifecycle)
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

    override fun showStreams(){
        editTextSearch?.text?.clear()
        childFragmentManager.setFragmentResult(
            SUBSCRIBE_RESULT_KEY,
            bundleOf(SHOW_STREAMS_KEY to showStreams)
        )
    }

    override fun backPressed(){
        onBackPressedCallback?.isEnabled = false
        requireActivity().onBackPressed()
    }

    override fun showResultSearch(topic: Topic) {
        Snackbar.make(requireView(), topic.name, Snackbar.LENGTH_SHORT).show()

        viewPager?.currentItem = 0
        childFragmentManager.setFragmentResult(
            SUBSCRIBE_RESULT_KEY,
            bundleOf(
                TOPIC_KEY to topic.name,
                STREAM_KEY to topic.nameStream
            )
        )
    }

    override fun showIsEmptyResultSearch() {
        Snackbar.make(requireView(), "ничего не найдено", Snackbar.LENGTH_SHORT).show()
    }

    override fun showRefresh() {}

    override fun hideRefresh() {}

    override fun showError() {
        Snackbar.make(requireView(), "ERROR", Snackbar.LENGTH_SHORT).show()
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