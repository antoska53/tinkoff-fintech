package ru.myacademyhomework.tinkoffmessenger.streamfragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import ru.myacademyhomework.tinkoffmessenger.database.ChatDatabase
import ru.myacademyhomework.tinkoffmessenger.database.StreamDb
import ru.myacademyhomework.tinkoffmessenger.database.TopicDb
import ru.myacademyhomework.tinkoffmessenger.FragmentNavigation
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.chatFragment.ChatFragment
import ru.myacademyhomework.tinkoffmessenger.data.ItemStream
import ru.myacademyhomework.tinkoffmessenger.data.Stream
import ru.myacademyhomework.tinkoffmessenger.network.RetrofitModule
import ru.myacademyhomework.tinkoffmessenger.network.Topic


class AllStreamFragment : Fragment(R.layout.fragment_all_stream) {

    private lateinit var adapter: StreamAdapter
    private var recycler: RecyclerView? = null
    private var navigation: FragmentNavigation? = null
    private var errorView: View? = null
    private val compositeDisposable = CompositeDisposable()
    private var shimmer: ShimmerFrameLayout? = null
    private var databaseIsNotEmpty = false
    private var databaseIsRefresh = false

    override fun onAttach(context: Context) {
        super.onAttach(context)

        require(context is FragmentNavigation)
        navigation = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        errorView = view.findViewById(R.id.error_view)
        val buttonReload = view.findViewById<Button>(R.id.button_reload)
        buttonReload.setOnClickListener { getStreams(view) }
        shimmer = view.findViewById(R.id.shimmer_stream_layout)

        setFragmentResultListener(StreamFragment.ALL_STREAM_RESULT_KEY) { key, bundle ->
            val result = bundle.getString(StreamFragment.TOPIC_KEY)
            if (result != null)
                adapter.setData(listOf(ItemStream(result, result)))
        }

        initRecycler(view)
        getStreamsFromDb(view)
    }

    private fun initRecycler(view: View) {
        recycler = view.findViewById(R.id.recycler_stream)
        adapter = StreamAdapter(
            streamListener = { streams, position, isSelected ->
                if (isSelected) updateStream(streams, position)
                else removeStream(streams, position)
            },
            topicListener = { topic ->
                openChatTopic(topic)
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

    private fun openChatTopic(topic: Topic) {
        navigation?.openChatFragment(
            ChatFragment.newInstance(
                topic.nameStream,
                topic.name,
            )
        )
    }

    private fun getStreamsFromDb(view: View) {
        val chatDao = ChatDatabase.getDatabase(requireContext()).chatDao()
        chatDao
            .getStreams()
            .map {
                it.map { streamDb ->
                    val listTopics = chatDao.getTopics(streamDb.nameChannel).map { topicDb ->
                        Topic(topicDb.streamId, topicDb.nameTopic, topicDb.nameStream)
                    }
                    Stream(streamDb.nameChannel, listTopics)
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isEmpty()) {
                    shimmer?.visibility = View.VISIBLE
                    shimmer?.startShimmer()
                    recycler?.visibility = View.GONE
                    errorView?.visibility = View.GONE
                    getStreams(view)
                } else {
                    databaseIsNotEmpty = true
                    adapter.setData(it)
                }
                if (!databaseIsRefresh) getStreams(view)
            }, {
            })
            .addTo(compositeDisposable)
    }

    private fun getStreams(view: View) {
        val chatApi = RetrofitModule.chatApi
        val chatDao = ChatDatabase.getDatabase(requireContext()).chatDao()
        chatApi.getStreams()
            .subscribeOn(Schedulers.io())
            .flatMap {
                Single.just(it.subscriptions)
            }
            .flatMapObservable {
                Observable.fromIterable(it)
            }
            .flatMap { subscription ->
                chatApi.getTopics(subscription.streamID)
                    .map { topicResponse ->
                        chatDao.insertTopics(topicResponse.topics.map { topic ->
                            TopicDb(topic.name, subscription.name, subscription.streamID)
                        })
                        StreamDb(subscription.streamID, subscription.name)
                    }
            }
            .toList()
            .doOnSuccess {
                chatDao.insertStream(it)
                databaseIsRefresh = true
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                if (!databaseIsNotEmpty) {
                    shimmer?.visibility = View.VISIBLE
                    shimmer?.startShimmer()
                    recycler?.visibility = View.GONE
                    errorView?.visibility = View.GONE
                }
            }
            .subscribe({
                shimmer?.stopShimmer()
                shimmer?.visibility = View.GONE
                recycler?.visibility = View.VISIBLE
                errorView?.visibility = View.GONE
            }, {
                if (databaseIsNotEmpty) {
                    Snackbar.make(
                        view,
                        "Неудалось обновить данные",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    shimmer?.stopShimmer()
                    shimmer?.visibility = View.GONE
                    recycler?.visibility = View.GONE
                    errorView?.visibility = View.VISIBLE
                }
            })
            .addTo(compositeDisposable)
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
        const val TYPE_STREAM = 0
        const val TYPE_TOPIC = 1

        @JvmStatic
        fun newInstance() = AllStreamFragment()
    }
}
