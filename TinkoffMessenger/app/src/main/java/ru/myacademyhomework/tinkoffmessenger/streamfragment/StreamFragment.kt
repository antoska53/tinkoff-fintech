package ru.myacademyhomework.tinkoffmessenger.streamfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.data.Stream
import ru.myacademyhomework.tinkoffmessenger.factory.StreamData
import ru.myacademyhomework.tinkoffmessenger.network.Topic
import java.util.concurrent.TimeUnit


class StreamFragment : Fragment(R.layout.fragment_stream) {
    private val subject = PublishSubject.create<String>()
    private var isSearch = false
    private var showStreams = true
    private val compositeDisposable = CompositeDisposable()
    private var editTextSearch: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (isSearch) {
                        isSearch = false
                        editTextSearch?.text?.clear()
                        childFragmentManager.setFragmentResult(
                            SUBSCRIBE_RESULT_KEY,
                            bundleOf(SHOW_STREAMS_KEY to showStreams)
                        )
                    } else {
                        isEnabled = false
                        requireActivity().onBackPressed()
                    }
                }
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabs: List<String> = listOf("Subscribed", "All streams")
        val pagerAdapter = PagerAdapter(childFragmentManager, lifecycle)
        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)

        val viewPager = view.findViewById<ViewPager2>(R.id.viewpager_stream)
        viewPager.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()

        editTextSearch = view.findViewById(R.id.search_edittext)
        editTextSearch?.addTextChangedListener { str ->
            subject.onNext(str.toString())
        }

        initSearch(view, viewPager)
    }

    private fun initSearch(view: View, viewPager: ViewPager2) {
            subject
                .filter { str -> str.isNotEmpty() }
                .distinctUntilChanged()
                .debounce(1000, TimeUnit.MILLISECONDS)
                .switchMap { str ->
                    val streams = StreamData.streams
                    var topic: Topic? = null
                    for (stream in streams) {
                        if (stream is Stream) {
                            topic = stream.topics.find {
                                it.name.equals(str, ignoreCase = true)
                            }
                            if (topic != null) return@switchMap Observable.just(topic)
                        }
                    }
                    Observable.just(Topic(0, ""))
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { topic ->
                        if (topic.name.isNotEmpty()) {
                            Snackbar.make(view, topic.name, Snackbar.LENGTH_SHORT).show()
                            viewPager.currentItem = 0
                            childFragmentManager.setFragmentResult(
                                SUBSCRIBE_RESULT_KEY,
                                bundleOf(TOPIC_KEY to topic.name,
                                        STREAM_KKEY to topic.nameStream)
                            )
                            isSearch = true
                        } else {
                            Snackbar.make(view, "ничего не найдено", Snackbar.LENGTH_SHORT).show()
                        }
                    },
                    {
                        Snackbar.make(view, "ERROR", Snackbar.LENGTH_SHORT).show()
                    }
                )
                .addTo(compositeDisposable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isSearch = false
        compositeDisposable.clear()
    }


    companion object {
        const val SUBSCRIBE_RESULT_KEY = "SUBSCRIBE_RESULT_KEY"
        const val ALL_STREAM_RESULT_KEY = "ALL_STREAM_RESULT_KEY"
        const val TOPIC_KEY = "STREAM_KEY"
        const val STREAM_KKEY = "STREAM_KKEY"
        const val SHOW_STREAMS_KEY = "SHOW_STREAMS_KEY"

        @JvmStatic
        fun newInstance() = StreamFragment()
    }
}