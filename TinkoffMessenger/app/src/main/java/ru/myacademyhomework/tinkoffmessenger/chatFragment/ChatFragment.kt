package ru.myacademyhomework.tinkoffmessenger.chatFragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.*
import androidx.activity.OnBackPressedDispatcher
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.myacademyhomework.tinkoffmessenger.ChatMessageListener
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.data.Message
import ru.myacademyhomework.tinkoffmessenger.factory.MessageFactory
import ru.myacademyhomework.tinkoffmessenger.factory.SubscribeChannelFactory
import java.util.concurrent.TimeUnit
import kotlin.random.Random


class ChatFragment : Fragment(R.layout.fragment_chat), ChatMessageListener {
    private val nameChannel by lazy {
        arguments?.getString(NAME_CHANNEL)
    }
    private val nameTopic by lazy {
        arguments?.getString(NAME_TOPIC)
    }
    private lateinit var adapter: ChatAdapter
    private var recyclerView: RecyclerView? = null
    private lateinit var editTextMessage: EditText
    private lateinit var buttonSendMessage: ImageButton
    private lateinit var dialog: BottomSheetDialog
    private val compositeDisposable = CompositeDisposable()
    private var errorView: View? = null
    private var shimmer: ShimmerFrameLayout? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        errorView = view.findViewById(R.id.error_view)
        shimmer = view.findViewById(R.id.shimmer_chat_layout)
        initRecycler(view)
        getMessages()

        val tvNameTopic = view.findViewById<TextView>(R.id.textview_name_topic)
        tvNameTopic.text = getString(R.string.topic, nameTopic)
        val tvNameChannel = view.findViewById<TextView>(R.id.textview_name_channel)
        tvNameChannel.text = nameChannel
        val buttonReload = view.findViewById<Button>(R.id.button_reload)
        buttonReload.setOnClickListener { getMessages() }

        val buttonBack = view.findViewById<ImageView>(R.id.imageView_arrow_back)
        buttonBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        buttonSendMessage = view.findViewById(R.id.button_send_message)
        buttonSendMessage.setOnClickListener { onClickButtonSendMessage() }
        editTextMessage = view.findViewById(R.id.edittext_message)
        editTextMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

            override fun afterTextChanged(s: Editable) {
                buttonSendMessage.setImageResource(
                    if (s.toString().isNotEmpty()) R.drawable.ic_plane else R.drawable.ic_cross
                )
            }
        })
    }

    override fun onDestroyView() {
        recyclerView?.adapter = null
        super.onDestroyView()
    }


    private fun initRecycler(view: View) {
        recyclerView = view.findViewById<RecyclerView>(R.id.chat_recycler)
        adapter = ChatAdapter(this)
        recyclerView?.adapter = adapter
    }

    private fun getMessages() {
        val disposable =
            Single.just(MessageFactory.messages)
                .subscribeOn(Schedulers.io())
                .delay(1000, TimeUnit.MILLISECONDS)
                .doOnSuccess {
                    val randomValue = Random.nextInt(0, 3)
                    if (randomValue == 0)
                        throw IllegalArgumentException()
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    shimmer?.visibility = View.VISIBLE
                    shimmer?.startShimmer()
                    recyclerView?.visibility = View.GONE
                    errorView?.visibility = View.GONE
                }
                .subscribe({
                    shimmer?.stopShimmer()
                    shimmer?.visibility = View.GONE
                    recyclerView?.visibility = View.VISIBLE
                    errorView?.visibility = View.GONE
                    adapter.addData(it)
                }, {
                    shimmer?.stopShimmer()
                    shimmer?.visibility = View.GONE
                    recyclerView?.visibility = View.GONE
                    errorView?.visibility = View.VISIBLE
                })
        compositeDisposable.add(disposable)

    }

    private fun onClickButtonSendMessage() {
        if (editTextMessage.text.isNotEmpty()) {
            val message = Message(
                avatar = "",
                name = "Me",
                message = editTextMessage.text.toString(),
                listEmoji = mutableListOf()
            )

            val disposable =
                sendMessage(message)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            updateRecycler(it)
                            editTextMessage.text.clear()
                        },
                        {
                            Snackbar.make(
                                recyclerView!!,
                                "Сообщение не отправлено",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    )
            compositeDisposable.add(disposable)
        }
    }

    private fun sendMessage(message: Message): Observable<Message> {
        val randomValue = Random.nextInt(1, 5)
        if (randomValue == 1)
            return Observable.error(IllegalArgumentException())
        MessageFactory.messages.add(message)
        return Observable.just(message)
    }

    override fun itemLongClicked(idMessage: Int): Boolean {
        showBottomSheetDialog(idMessage)
        return true
    }


    private fun showBottomSheetDialog(idMessage: Int) {
        val bottomSheet = layoutInflater.inflate(R.layout.bottom_sheet, null)
        dialog = BottomSheetDialog(this.requireContext(), R.style.BottomSheetDialogTheme)
        dialog.setContentView(bottomSheet)


        val recyclerBottomSheet = bottomSheet.findViewById<RecyclerView>(R.id.bottom_sheet_recycler)
        val adapterBottomSheet = BottomSheetAdapter(idMessage) { emoji, id ->
            updateEmoji(emoji, id)
            dialog.dismiss()
        }
        recyclerBottomSheet.adapter = adapterBottomSheet
        dialog.show()
    }

    private fun updateRecycler(message: Message) {
        adapter.updateData(message)
        recyclerView?.smoothScrollToPosition(adapter.itemCount - 1)
    }


    private fun updateEmoji(emoji: String, idMessage: Int) {
        val message = MessageFactory.createMessage()[idMessage]
        if (message is Message) {
            message.listEmoji.add(emoji)
            adapter.updateListEmoji(idMessage)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    companion object {
        const val TYPE_DATE = 0
        const val TYPE_MESSAGE = 1
        const val NAME_CHANNEL = "NAME_CHANNEL"
        const val NAME_TOPIC = "NAME_STREAM"

        @JvmStatic
        fun newInstance(nameChannel: String, nameTopic: String) =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putString(NAME_CHANNEL, nameChannel)
                    putString(NAME_TOPIC, nameTopic)
                }
            }
    }
}