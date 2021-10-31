package ru.myacademyhomework.tinkoffmessenger.streamfragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.chatFragment.ChatFragment
import ru.myacademyhomework.tinkoffmessenger.data.ItemStream
import ru.myacademyhomework.tinkoffmessenger.factory.SubscribeChannelFactory


class SubscribedFragment : Fragment(R.layout.fragment_subscribed) {

    private lateinit var adapter: StreamAdapter
    private var recycler: RecyclerView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler(view)
    }

    private fun initRecycler(view: View){
        recycler = view.findViewById<RecyclerView>(R.id.recycler_subscribe_stream)
        adapter = StreamAdapter(
            { streams, position, isSelected ->
                if (isSelected) updateStream(streams, position)
                else removeStream(streams, position)
            }, { stream ->
                openChatTopic(stream)
            }
        )
        adapter.channels = SubscribeChannelFactory.channels
        recycler?.adapter = adapter
    }

    private fun updateStream(streams: List<ItemStream>, position: Int){
        SubscribeChannelFactory.channels.addAll(position + 1, streams)
        adapter.updateData(position, streams.size, false)
    }

    private fun removeStream(streams: List<ItemStream>, position: Int){
        SubscribeChannelFactory.channels.removeAll(streams)
        adapter.updateData(position, streams.size, true)
    }

    private fun openChatTopic(stream: ItemStream) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.flow_fragment_container, ChatFragment.newInstance(stream.nameChannel, stream.nameStream))
            .addToBackStack("SUBSCRIBE")
            .commitAllowingStateLoss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recycler?.adapter = null
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            SubscribedFragment()
    }
}