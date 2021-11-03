package ru.myacademyhomework.tinkoffmessenger.streamfragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.myacademyhomework.tinkoffmessenger.FragmentNavigation
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.chatFragment.ChatFragment
import ru.myacademyhomework.tinkoffmessenger.data.ItemStream
import ru.myacademyhomework.tinkoffmessenger.factory.ChannelFactory


class AllStreamFragment : Fragment(R.layout.fragment_all_stream) {

    private lateinit var adapter: StreamAdapter
    private var recycler: RecyclerView? = null
    private var navigation: FragmentNavigation? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentNavigation)
            navigation = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler(view)
    }

    private fun initRecycler(view: View) {
        recycler = view.findViewById<RecyclerView>(R.id.recycler_stream)

        adapter = StreamAdapter(
            { streams, position, isSelected ->
                if (isSelected) updateStream(streams, position)
                else removeStream(streams, position)
            }, { stream ->
                openChatTopic(stream)
            }
        )
        adapter.setData(ChannelFactory.channels)
        recycler?.adapter = adapter
    }

    private fun updateStream(streams: List<ItemStream>, position: Int) {
        adapter.updateData(streams, position, false)
    }

    private fun removeStream(streams: List<ItemStream>, position: Int) {
        adapter.updateData(streams, position, true)
    }

    private fun openChatTopic(stream: ItemStream) {
        navigation?.changeFlowFragment(ChatFragment.newInstance(stream.nameChannel, stream.nameStream))

    }

    override fun onDestroyView() {
        super.onDestroyView()
        recycler?.adapter = null
    }

    companion object {
        const val TYPE_ITEM_CHANNEL = 0
        const val TYPE_STREAM = 1

        @JvmStatic
        fun newInstance() =
            AllStreamFragment()
    }
}