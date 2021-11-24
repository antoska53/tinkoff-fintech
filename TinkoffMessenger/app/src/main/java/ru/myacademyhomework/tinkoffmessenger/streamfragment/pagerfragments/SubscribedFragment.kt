package ru.myacademyhomework.tinkoffmessenger.streamfragment.pagerfragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import ru.myacademyhomework.tinkoffmessenger.database.ChatDatabase
import ru.myacademyhomework.tinkoffmessenger.FragmentNavigation
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.chatFragment.ChatFragment
import ru.myacademyhomework.tinkoffmessenger.data.Stream
import ru.myacademyhomework.tinkoffmessenger.network.Topic
import ru.myacademyhomework.tinkoffmessenger.streamfragment.StreamFragment


class SubscribedFragment : MvpAppCompatFragment(R.layout.fragment_subscribed), PagerView {

    private lateinit var adapter: StreamAdapter
    private var recycler: RecyclerView? = null
    private var navigation: FragmentNavigation? = null
    private var errorView: View? = null
    private var shimmer: ShimmerFrameLayout? = null
    private val pagerPresenter by moxyPresenter {
        val chatDao = ChatDatabase.getDatabase(requireContext()).chatDao()
        PagerPresenter(chatDao)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        require(context is FragmentNavigation)
        navigation = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        errorView = view.findViewById(R.id.error_view)
        val buttonReload = view.findViewById<Button>(R.id.button_reload)
        buttonReload.setOnClickListener { pagerPresenter.getStreams() }
        shimmer = view.findViewById(R.id.shimmer_stream_layout)


        setFragmentResultListener(StreamFragment.SUBSCRIBE_RESULT_KEY) { key, bundle ->
            val resultTopic = bundle.getString(StreamFragment.TOPIC_KEY)
            val resultStream = bundle.getString(StreamFragment.STREAM_KEY)
            val resultShowStreams = bundle.getBoolean(StreamFragment.SHOW_STREAMS_KEY)
            if (resultTopic != null && resultStream != null)
                adapter.setData(listOf(Topic(0, resultTopic, resultStream)))
            if (resultShowStreams) {
                pagerPresenter.getStreams()
            }
        }

        initRecycler(view)
        pagerPresenter.getStreamsFromDb()
    }

    private fun initRecycler(view: View) {
        recycler = view.findViewById(R.id.recycler_subscribe_stream)
        adapter = StreamAdapter(
            { streams, position, isSelected ->
                if (isSelected) updateStream(streams, position)
                else removeStream(streams, position)
            }, { topic ->
                pagerPresenter.openChatTopic(topic)
            }
        )
        recycler?.adapter = adapter
    }

    private fun updateStream(topics: List<Topic>, position: Int) {
        adapter.updateData(topics, position, false)
    }

    private fun removeStream(topics: List<Topic>, position: Int) {
        adapter.updateData(topics, position, true)
    }

    override fun setDataToRecycler(listStream: List<Stream>) {
        adapter.setData(listStream)
    }

    override fun openChatTopic(topic: Topic) {
        navigation?.openChatFragment(
            ChatFragment.newInstance(
                topic.nameStream,
                topic.name,
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recycler?.adapter = null
    }

    override fun onDetach() {
        super.onDetach()
        navigation = null
    }

    override fun showRefresh() {
        shimmer?.isVisible = true
        shimmer?.startShimmer()
        recycler?.isVisible = false
        errorView?.isVisible = false
    }

    override fun hideRefresh() {
        shimmer?.stopShimmer()
        shimmer?.isVisible = false
        recycler?.isVisible = true
        errorView?.isVisible = false
    }

    override fun showError() {
        shimmer?.stopShimmer()
        shimmer?.isVisible = false
        recycler?.isVisible = false
        errorView?.isVisible = true
    }

    override fun showErrorUpdateData() {
        Snackbar.make(
            requireView(),
            "Неудалось обновить данные",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    companion object {
        @JvmStatic
        fun newInstance() = SubscribedFragment()
    }
}
