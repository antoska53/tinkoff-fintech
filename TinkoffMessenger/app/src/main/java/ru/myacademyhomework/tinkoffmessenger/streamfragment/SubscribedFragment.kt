package ru.myacademyhomework.tinkoffmessenger.streamfragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import ru.myacademyhomework.tinkoffmessenger.FragmentNavigation
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.chatFragment.ChatFragment
import ru.myacademyhomework.tinkoffmessenger.data.ItemStream
import ru.myacademyhomework.tinkoffmessenger.factory.SubscribeChannelFactory


class SubscribedFragment : Fragment(R.layout.fragment_subscribed) {

    private lateinit var adapter: StreamAdapter
    private var recycler: RecyclerView? = null
    private var navigation: FragmentNavigation? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        require(context is FragmentNavigation)
        navigation = context
    }

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
        adapter.setData(SubscribeChannelFactory.createChannel())
        recycler?.adapter = adapter
    }

    private fun updateStream(streams: List<ItemStream>, position: Int){
        adapter.updateData(streams , position, false)
    }

    private fun removeStream(streams: List<ItemStream>, position: Int){
        adapter.updateData(streams, position, true)
    }

    private fun openChatTopic(stream: ItemStream) {
        navigation?.openChatFragment(ChatFragment.newInstance(stream.nameChannel, stream.nameStream))

    }

    override fun onDestroyView() {
        super.onDestroyView()
        recycler?.adapter = null
    }

    companion object {

        @JvmStatic
        fun newInstance() = SubscribedFragment()
    }
}