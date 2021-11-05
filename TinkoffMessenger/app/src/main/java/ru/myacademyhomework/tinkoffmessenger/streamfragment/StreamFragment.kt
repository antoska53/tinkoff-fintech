package ru.myacademyhomework.tinkoffmessenger.streamfragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResult
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.data.ItemChannel
import ru.myacademyhomework.tinkoffmessenger.data.ItemStream
import ru.myacademyhomework.tinkoffmessenger.factory.ChannelFactory
import java.util.concurrent.TimeUnit


class StreamFragment : Fragment(R.layout.fragment_stream) {
    private val subject = PublishSubject.create<String>()
    private var isSearch = false
    private var showStreams = true
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, object :OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if (isSearch){
                    Log.d("RXSEARCH", "handleOnBackPressed: $isSearch")
                    isSearch = false
                  //  editTextSearch.text.clear()
                    childFragmentManager.setFragmentResult(
                        SUBSCRIBE_RESULT_KEY,
                        bundleOf(SHOW_STREAMS_KEY to showStreams)
                    )
                }
                else{
                    isEnabled = false
                    activity?.onBackPressed()
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

        val editTextSearch = view.findViewById<EditText>(R.id.search_edittext)
        editTextSearch.addTextChangedListener { str ->
            subject.onNext(str.toString())
            Log.d("RXSEARCH", "Listener: $str")
        }


        val disposable =
            subject
                .filter { str -> str.isNotEmpty() }
                .distinctUntilChanged()
                .debounce(1000, TimeUnit.MILLISECONDS)
                .switchMap { str ->
                    Log.d("RXSEARCH", "onViewCreated1: $str")
                    val channels = ChannelFactory.channels
                    var stream: ItemStream? = null
                    for (channel in channels) {
                        Log.d("RXSEARCH", "For channel: $channel")
                        if (channel is ItemChannel) {
                            stream = channel.streams.find {
                                Log.d(
                                    "RXSEARCH",
                                    "Equals: name= ${it.nameStream} str= $str ${it.nameStream == str}"
                                )
                                it.nameStream.equals(str, ignoreCase = true)
                            }
                            Log.d("RXSEARCH", "onViewCreated2: $stream")
                            if (stream != null) return@switchMap Observable.just(stream)
                        }
                    }
                    Observable.just(ItemStream("", ""))
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { itemStream ->
                        if (itemStream.nameStream.isNotEmpty()) {
                            Snackbar.make(view, itemStream.nameStream, Snackbar.LENGTH_SHORT).show()
                            val selectedPage = viewPager.currentItem
                            if (selectedPage == 0){
                                childFragmentManager.setFragmentResult(
                                    SUBSCRIBE_RESULT_KEY,
                                    bundleOf(STREAM_KEY to itemStream.nameStream)
                                )
                            }else{
                                childFragmentManager.setFragmentResult(
                                    ALL_STREAM_RESULT_KEY,
                                    bundleOf(STREAM_KEY to itemStream.nameStream)
                                )
                            }
                            isSearch = true
                        } else {
                            Snackbar.make(view, "ничего не найдено", Snackbar.LENGTH_SHORT).show()
                        }
                    },
                    {
                        Log.d("RXSEARCH", "onViewCreated: ОШИБКА!!!!!")
                        Log.d("RXSEARCH", "onViewCreated: ОШИБКА!!!!! $it")
                        Snackbar.make(view, "ERROR", Snackbar.LENGTH_SHORT).show()
                    }
                )

        compositeDisposable.add(disposable)

    }

    override fun onDetach() {
        super.onDetach()
        Log.d("RXSEARCH", "onDetach: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d("RXSEARCH", "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d("RXSEARCH", "onStop: ")
//        isSearch = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("RXSEARCH", "onDestroyView: ")
        isSearch = false
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
//        isSearch = false
    }


    companion object {
        const val SUBSCRIBE_RESULT_KEY = "SUBSCRIBE_RESULT_KEY"
        const val ALL_STREAM_RESULT_KEY = "ALL_STREAM_RESULT_KEY"
        const val STREAM_KEY = "STREAM_KEY"
        const val SHOW_STREAMS_KEY ="SHOW_STREAMS_KEY"
        @JvmStatic
        fun newInstance() = StreamFragment()
    }
}