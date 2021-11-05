package ru.myacademyhomework.tinkoffmessenger.streamfragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import ru.myacademyhomework.tinkoffmessenger.FragmentNavigation
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.chatFragment.ChatFragment
import ru.myacademyhomework.tinkoffmessenger.data.ItemStream
import ru.myacademyhomework.tinkoffmessenger.factory.ChannelFactory
import java.util.concurrent.TimeUnit


class AllStreamFragment : Fragment(R.layout.fragment_all_stream) {

    private lateinit var adapter: StreamAdapter
    private var recycler: RecyclerView? = null
    private var navigation: FragmentNavigation? = null
    private var errorView: View? = null
    private val compositeDisposable = CompositeDisposable()
    private var shimmer: ShimmerFrameLayout? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        require(context is FragmentNavigation)
        navigation = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        errorView = view.findViewById(R.id.error_view)
        val buttonReload = view.findViewById<Button>(R.id.button_reload)
        buttonReload.setOnClickListener { getStreams() }
        shimmer = view.findViewById(R.id.shimmer_stream_layout)

        setFragmentResultListener(StreamFragment.ALL_STREAM_RESULT_KEY){ key, bundle ->
            val result = bundle.getString(StreamFragment.STREAM_KEY)
            if (result != null)
                adapter.setData(listOf(ItemStream(result, result)))
        }

        initRecycler(view)
        getStreams()

    }

    private fun initRecycler(view: View) {
        recycler = view.findViewById<RecyclerView>(R.id.recycler_stream)

        adapter = StreamAdapter(
            channelListener = { streams, position, isSelected ->
                if (isSelected) updateStream(streams, position)
                else removeStream(streams, position)
            },
            streamListener = { stream ->
                openChatTopic(stream)
            }
        )
        adapter.setData(ChannelFactory.createChannel())
        recycler?.adapter = adapter
    }

    private fun updateStream(streams: List<ItemStream>, position: Int) {
        adapter.updateData(streams, position, false)
    }

    private fun removeStream(streams: List<ItemStream>, position: Int) {
        adapter.updateData(streams, position, true)
    }

    private fun openChatTopic(stream: ItemStream) {
        navigation?.openChatFragment(
            ChatFragment.newInstance(
                stream.nameChannel,
                stream.nameStream
            )
        )

    }

    private fun getStreams() {

        val disposable =
            Single.just(ChannelFactory.channels)
                .subscribeOn(Schedulers.io())
                .delay(3000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    shimmer?.visibility = View.VISIBLE
                    shimmer?.startShimmer()
                    recycler?.visibility = View.GONE
                    errorView?.visibility = View.GONE
                }
                .subscribe({
                    shimmer?.stopShimmer()
                    shimmer?.visibility = View.GONE
                    recycler?.visibility = View.VISIBLE
                    errorView?.visibility = View.GONE
                    adapter.setData(it)
                }, {
                    recycler?.visibility = View.GONE
                    errorView?.visibility = View.VISIBLE
                })

        compositeDisposable.add(disposable)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        recycler?.adapter = null
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    override fun onDetach() {
        super.onDetach()
        navigation = null
    }

    companion object {
        const val TYPE_ITEM_CHANNEL = 0
        const val TYPE_STREAM = 1

        @JvmStatic
        fun newInstance() = AllStreamFragment()
    }
}