package ru.myacademyhomework.tinkoffmessenger.streamfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.factory.ChannelFactory


class AllStreamFragment : Fragment(R.layout.fragment_all_stream) {

    private lateinit var adapter: StreamAdapter
    private var recycler: RecyclerView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler(view)
    }

    private fun initRecycler(view: View){
        recycler = view.findViewById<RecyclerView>(R.id.recycler_stream)
        adapter = StreamAdapter(){ streams, position, isSelected ->
            if (isSelected) updateStream(streams, position)
            else removeStream(streams, position)
        }
        adapter.channels = ChannelFactory.channels
        recycler?.adapter = adapter
    }

    private fun updateStream(streams: List<String>, position: Int , nameChannel: String){
        ChannelFactory.channels.addAll(position + 1, streams)
        adapter.updateData(position, streams.size, false, nameChannel)
    }

    private fun removeStream(streams: List<String>, position: Int){
        ChannelFactory.channels.removeAll(streams)
        adapter.updateData(position, streams.size, true)
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