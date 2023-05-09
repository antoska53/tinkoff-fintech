package ru.myacademyhomework.tinkoffmessenger.presentation.streamfragment.pagerfragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import ru.myacademyhomework.tinkoffmessenger.presentation.App
import ru.myacademyhomework.tinkoffmessenger.presentation.FragmentNavigation
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.presentation.chatfragment.ChatFragment
import ru.myacademyhomework.tinkoffmessenger.domain.pager.Stream
import ru.myacademyhomework.tinkoffmessenger.presentation.listeners.StreamListener
import ru.myacademyhomework.tinkoffmessenger.data.network.model.TopicDto
import ru.myacademyhomework.tinkoffmessenger.domain.pager.Topic
import ru.myacademyhomework.tinkoffmessenger.presentation.streamfragment.newstreamfragment.NewStreamFragment
import javax.inject.Inject
import javax.inject.Provider


class AllStreamFragment : MvpAppCompatFragment(R.layout.fragment_all_stream), PagerView, StreamListener {

    private lateinit var adapter: StreamAdapter
    private var recycler: RecyclerView? = null
    private var navigation: FragmentNavigation? = null
    private var errorView: View? = null
    private var shimmer: ShimmerFrameLayout? = null

    @Inject
    lateinit var providePresenter: Provider<PagerPresenter>
    private val pagerPresenter by moxyPresenter {
        providePresenter.get()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity?.application as App).appComponent.getPagerComponent().inject(this)
        super.onCreate(savedInstanceState)
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
        buttonReload.setOnClickListener { pagerPresenter.getSubscribeStreams() }
        val buttonNewStream = view.findViewById<MaterialButton>(R.id.button_new_stream)
        buttonNewStream.setOnClickListener { pagerPresenter.openNewStreamFragment() }
        shimmer = view.findViewById(R.id.shimmer_stream_layout)

        initRecycler(view)
        pagerPresenter.getAllStreamsFromDb()
    }

    private fun initRecycler(view: View) {
        recycler = view.findViewById(R.id.recycler_stream)
        adapter = StreamAdapter(
            streamListener = this,
            topicListener = { topic ->
                openChatTopic(topic)
            }
        )
        recycler?.adapter = adapter
    }

    private fun updateStream(topics: List<Topic>, position: Int, isSelected: Boolean) {
        adapter.updateData(topics, position, isSelected)
    }

    private fun removeStream(topics: List<Topic>, position: Int, isSelected: Boolean) {
        adapter.updateData(topics, position, isSelected)
    }

    override fun setDataToRecycler(listStream: List<Stream>) {
        val streamDiffUtilCallback = StreamDiffUtilCallback(adapter.streams, listStream)
        val streamDiffResult = DiffUtil.calculateDiff(streamDiffUtilCallback)
        adapter.setData(listStream)
        streamDiffResult.dispatchUpdatesTo(adapter)
    }

    override fun openChatTopic(topic: Topic) {
        navigation?.openChatFragment(
            ChatFragment.newInstance(
                topic.nameStream,
                topic.name,
            )
        )
    }

    override fun openChatStream(stream: Stream) {
        navigation?.openChatFragment(
            ChatFragment.newInstance(
                stream.nameChannel,
                ChatFragment.STREAM_CHAT
            )
        )
    }

    override fun openNewStreamFragment(){
        navigation?.changeFlowFragment(
            NewStreamFragment.newInstance(),
            true
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
            getString(R.string.error_update_data),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun itemStreamArrowClicked(topics: List<Topic>, position: Int, isSelected: Boolean) {
        if (isSelected) updateStream(topics, position, isSelected)
        else removeStream(topics, position, isSelected)
    }

    override fun itemStreamClicked(stream: Stream) {
        pagerPresenter.openChatStream(stream)
    }

    companion object {
        const val TYPE_STREAM = 0
        const val TYPE_TOPIC = 1

        @JvmStatic
        fun newInstance() = AllStreamFragment()
    }
}
