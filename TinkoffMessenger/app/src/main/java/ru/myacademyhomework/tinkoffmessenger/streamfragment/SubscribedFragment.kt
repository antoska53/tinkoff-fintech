package ru.myacademyhomework.tinkoffmessenger.streamfragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import ru.myacademyhomework.tinkoffmessenger.Database.ChatDatabase
import ru.myacademyhomework.tinkoffmessenger.Database.StreamDb
import ru.myacademyhomework.tinkoffmessenger.Database.TopicDb
import ru.myacademyhomework.tinkoffmessenger.FragmentNavigation
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.chatFragment.ChatFragment
import ru.myacademyhomework.tinkoffmessenger.data.Stream
import ru.myacademyhomework.tinkoffmessenger.factory.StreamData
import ru.myacademyhomework.tinkoffmessenger.network.RetrofitModule
import ru.myacademyhomework.tinkoffmessenger.network.Topic
import java.util.concurrent.TimeUnit


class SubscribedFragment : Fragment(R.layout.fragment_subscribed) {

    private lateinit var adapter: StreamAdapter
    private var recycler: RecyclerView? = null
    private var navigation: FragmentNavigation? = null
    private var errorView: View? = null
    private var shimmer: ShimmerFrameLayout? = null
    private val compositeDisposable = CompositeDisposable()

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


        setFragmentResultListener(StreamFragment.SUBSCRIBE_RESULT_KEY) { key, bundle ->
            val resultTopic = bundle.getString(StreamFragment.TOPIC_KEY)
            val resultStream = bundle.getString(StreamFragment.STREAM_KKEY)
            val resultShowStreams = bundle.getBoolean(StreamFragment.SHOW_STREAMS_KEY)
            if (resultTopic != null && resultStream != null)
                adapter.setData(listOf(Topic( 0, resultTopic, resultStream)))
            if (resultShowStreams) {
                getStreams(view)
            }
        }

        initRecycler(view)
        getStreamsFromDb()
        getStreams(view)
    }

    private fun initRecycler(view: View) {
        recycler = view.findViewById(R.id.recycler_subscribe_stream)
        adapter = StreamAdapter(
            { streams, position, isSelected ->
                if (isSelected) updateStream(streams, position)
                else removeStream(streams, position)
            }, { topic ->
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
                topic.streamId
            )
        )
    }

    private fun getStreamsFromDb(){
        val chatDao = ChatDatabase.getDatabase(requireContext()).chatDao()
        val disposable =
            chatDao
            .getStreams()
            .map {
               it.map {streamDb ->
                   val listTopics = chatDao.getTopics(streamDb.nameChannel).map{ topicDb ->
                       Topic(topicDb.streamId, topicDb.nameTopic, topicDb.nameStream)
                   }
                   Stream(streamDb.nameChannel, listTopics)
               }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("GETSTREAMS", "getStreamsFromDb: onSUcces $it")
                if (it.isEmpty()){
                    shimmer?.visibility = View.VISIBLE
                    shimmer?.startShimmer()
                    recycler?.visibility = View.GONE
                    errorView?.visibility = View.GONE
                }else{
                    adapter.setData(it)
                }
            },{
                Log.d("GETSTREAMS", "getStreamsFromDb: ERROR $it")
            })
        compositeDisposable.add(disposable)
    }

    private fun getStreams(view: View) {
        val chatApi = RetrofitModule.chatApi
        val chatDao = ChatDatabase.getDatabase(requireContext()).chatDao()
        val disposable =
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
                            chatDao.insertTopics(topicResponse.topics.map{ topic ->
                                    TopicDb(topic.name, subscription.name , subscription.streamID)
                            })
                            StreamDb(subscription.streamID, subscription.name)
                        }
                }
                .toList()
                .doOnSuccess {
                    chatDao.insertStream(it)
                }
                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe {
//                    shimmer?.visibility = View.VISIBLE
//                    shimmer?.startShimmer()
//                    recycler?.visibility = View.GONE
//                    errorView?.visibility = View.GONE
//                }
                .subscribe({
                    shimmer?.stopShimmer()
                    shimmer?.visibility = View.GONE
                    recycler?.visibility = View.VISIBLE
                    errorView?.visibility = View.GONE
                    StreamData.streams.clear()
                    StreamData.streams.addAll(it)
                    adapter.setData(it)
                }, {
                    Log.d("GETSTREAMS", "getStreams: ERROR $it")
                    shimmer?.stopShimmer()
                    shimmer?.visibility = View.GONE
//                    recycler?.visibility = View.GONE
//                    errorView?.visibility = View.VISIBLE
                    Snackbar.make(
                        view,
                        "Неудалось обновить данные",
                        Snackbar.LENGTH_SHORT
                    ).show()
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
        @JvmStatic
        fun newInstance() = SubscribedFragment()
    }
}