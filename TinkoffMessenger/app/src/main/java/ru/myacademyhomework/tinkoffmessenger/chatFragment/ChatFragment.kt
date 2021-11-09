package ru.myacademyhomework.tinkoffmessenger.chatFragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.delay
import ru.myacademyhomework.tinkoffmessenger.ChatMessageListener
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.data.Emoji
import ru.myacademyhomework.tinkoffmessenger.network.RetrofitModule
import ru.myacademyhomework.tinkoffmessenger.network.UserMessage


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
    private var isInitRecycler = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        errorView = view.findViewById(R.id.error_view)
        shimmer = view.findViewById(R.id.shimmer_chat_layout)
        initRecycler(view)

        val tvNameTopic = view.findViewById<TextView>(R.id.textview_name_topic)
        tvNameTopic.text = getString(R.string.topic, nameTopic)
        val tvNameChannel = view.findViewById<TextView>(R.id.textview_name_channel)
        tvNameChannel.text = nameChannel
        val buttonReload = view.findViewById<Button>(R.id.button_reload)
        buttonReload.setOnClickListener {
            if(isInitRecycler) {
                getMessages(nameChannel!!, nameTopic!!)
            }else{
                initRecycler(view)
            }
        }

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
        val disposable =
        RetrofitModule.chatApi.getOwnUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                errorView?.visibility = View.GONE
                isInitRecycler = true
                recyclerView = view.findViewById(R.id.chat_recycler)
                adapter = ChatAdapter(this, it.userID)
                recyclerView?.adapter = adapter
                getMessages(nameChannel!!, nameTopic!!)
            },{
                errorView?.visibility = View.VISIBLE
            })
        compositeDisposable.add(disposable)
    }

    private fun getMessages(streamName: String, topicName: String) {
        val chatApi = RetrofitModule.chatApi
        val disposable =
                chatApi.getMessages(
                "newest",
                5,
                15,
                "[{\"operand\":\"$streamName\", \"operator\":\"stream\"},{\"operand\":\"$nameTopic\",\"operator\":\"topic\"}]"
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    shimmer?.isVisible = true
                    shimmer?.startShimmer()
                    recyclerView?.isVisible = false
                    errorView?.isVisible = false
                }
                .doOnTerminate {
                    shimmer?.stopShimmer()
                    shimmer?.isVisible = false
                }
                .subscribe({
                    adapter.addData(it.message)
                    recyclerView?.isVisible = true
                    errorView?.isVisible = false
                }, {
                    errorView?.isVisible = true
                    recyclerView?.isVisible = false
                })
        compositeDisposable.add(disposable)
    }

    private fun getMessage(messageId: Int, position: Int){
        val chatApi = RetrofitModule.chatApi
        val disposable =
            chatApi.getMessages(
                "newest",
                5,
                15,
                "[{\"operand\":\"$nameChannel\", \"operator\":\"stream\"},{\"operand\":\"$nameTopic\",\"operator\":\"topic\"},{\"operand\":\"$messageId\",\"operator\":\"id\"}]"
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if(position < 0){
                        updateRecycler(it.messages[0])
                    }else{
                        updateRecycler(it.messages[0], position)
                    }
                }, {
                })
        compositeDisposable.add(disposable)
    }

    private fun onClickButtonSendMessage() {
        if (editTextMessage.text.isNotEmpty()) {
            sendMessage( message = editTextMessage.text.toString())
        }
    }


    private fun sendMessage(message: String){
        val disposable =
        RetrofitModule.chatApi.sendMessage("stream", nameChannel!!, nameTopic!!, message)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    editTextMessage.text.clear()
                    getMessage(it.id, -1)
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

    override fun itemLongClicked(idMessage: Int, position:Int): Boolean {
        showBottomSheetDialog(idMessage, position)
        return true
    }


    private fun showBottomSheetDialog(idMessage: Int, positionMessage:Int) {
        val bottomSheet = layoutInflater.inflate(R.layout.bottom_sheet, null)
        dialog = BottomSheetDialog(this.requireContext(), R.style.BottomSheetDialogTheme)
        dialog.setContentView(bottomSheet)


        val recyclerBottomSheet = bottomSheet.findViewById<RecyclerView>(R.id.bottom_sheet_recycler)
        val adapterBottomSheet = BottomSheetAdapter(idMessage, positionMessage) { emoji, id, position ->
            updateEmoji(emoji, id, position)
            dialog.dismiss()
        }
        recyclerBottomSheet.adapter = adapterBottomSheet
        dialog.show()
    }

    private fun updateRecycler(message: UserMessage) {
        adapter.updateData(message)
        recyclerView?.smoothScrollToPosition(adapter.itemCount - 1)
    }

    private fun updateRecycler(message: UserMessage, position: Int){
        adapter.updateListEmoji(message, position)
    }


    private fun updateEmoji(emoji: String, idMessage: Int, position: Int) {
        val emojiName =  Emoji.values().find {
            it.unicode == emoji
        }

        val disposable =
            RetrofitModule.chatApi.addReaction(idMessage, emojiName?.nameInZulip ?: "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    getMessage(idMessage, position)
                },
                    {
                        Snackbar.make(
                            recyclerView!!,
                            "Неудалось добавить эмодзи \uD83D\uDE2D",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    })
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