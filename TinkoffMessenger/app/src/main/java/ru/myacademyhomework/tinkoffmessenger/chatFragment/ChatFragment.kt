package ru.myacademyhomework.tinkoffmessenger.chatFragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import ru.myacademyhomework.tinkoffmessenger.ChatMessageListener
import ru.myacademyhomework.tinkoffmessenger.database.ChatDatabase
import ru.myacademyhomework.tinkoffmessenger.database.MessageDb
import ru.myacademyhomework.tinkoffmessenger.database.ReactionDb
import ru.myacademyhomework.tinkoffmessenger.database.UserDb
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.data.Emoji
import ru.myacademyhomework.tinkoffmessenger.network.Reaction
import ru.myacademyhomework.tinkoffmessenger.network.RetrofitModule
import ru.myacademyhomework.tinkoffmessenger.network.User
import ru.myacademyhomework.tinkoffmessenger.network.UserMessage


class ChatFragment : Fragment(R.layout.fragment_chat), ChatMessageListener {

    private val nameStream by lazy {
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
    private var databaseIsRefresh = false
    private var databaseIsNotEmpty = false
    private var isLoading = true
    private var firstMessageId = "newest"
    private var foundOldest = false
    private var oldMessageSubscriber: Flowable<List<UserMessage>>? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        errorView = view.findViewById(R.id.error_view)
        shimmer = view.findViewById(R.id.shimmer_chat_layout)
        val sharedPref = requireContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        foundOldest = sharedPref.getBoolean(FOUND_OLDEST_KEY, false)
        initRecycler(view)

        val tvNameTopic = view.findViewById<TextView>(R.id.textview_name_topic)
        tvNameTopic.text = getString(R.string.topic, nameTopic)
        val tvNameChannel = view.findViewById<TextView>(R.id.textview_name_channel)
        tvNameChannel.text = nameStream
        val buttonReload = view.findViewById<Button>(R.id.button_reload)
        buttonReload.setOnClickListener {
            if (isInitRecycler) {
                getMessages(view, firstMessageId)
            } else {
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
        ChatDatabase.getDatabase(requireContext()).chatDao()
            .getOwnUser()
            .map {
                it.map { userDb ->
                    User(
                        avatarURL = userDb.avatarURL,
                        email = userDb.email,
                        fullName = userDb.fullName,
                        userID = userDb.userID
                    )
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isEmpty()) {
                    getOwnUser()
                } else {
                    errorView?.visibility = View.GONE
                    isInitRecycler = true
                    recyclerView = view.findViewById(R.id.chat_recycler)
                    adapter = ChatAdapter(this, it[0].userID)
                    recyclerView?.adapter = adapter
                    recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)
                            pagingChat(recyclerView, view)
                        }
                    })
                    getAllMessagesFromDb(view)
                }
            }, {
            })
            .addTo(compositeDisposable)
    }

    private fun pagingChat(recyclerView: RecyclerView, view: View){
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
        Log.d("PAGING", "pagingChat: $firstVisibleItem")
        Log.d("PAGING", "pagingChat: ${adapter.itemCount}")
        if (!isLoading) {
            if (!foundOldest) {
                if (firstVisibleItem - POSITION_FOR_LOAD <= FIRST_POSITION) {

                    isLoading = true
                    compositeDisposable.clear()
                    getOldMessageFromDb()
                    getMessages(view, firstMessageId)
                }
            }
        }
    }

    private fun getOwnUser() {
        val chatDao = ChatDatabase.getDatabase(requireContext()).chatDao()
        RetrofitModule.chatApi.getOwnUser()
            .subscribeOn(Schedulers.io())
            .map {
                UserDb(
                    avatarURL = it.avatarURL,
                    email = it.email,
                    fullName = it.fullName,
                    userID = it.userID,
                    isOwn = true
                )
            }
            .doOnSuccess {
                chatDao.insertOwnUser(it)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                errorView?.visibility = View.GONE
            }, {
                errorView?.visibility = View.VISIBLE
            })
            .addTo(compositeDisposable)
    }

    private fun getAllMessagesFromDb(view: View) {
        val chatDao = ChatDatabase.getDatabase(requireContext()).chatDao()
        chatDao.getAllMessages(nameTopic!!)
            .map {
                it.map { messageDb ->
                    UserMessage(
                        avatarURL = messageDb.avatarURL,
                        content = messageDb.content,
                        id = messageDb.id,
                        isMeMessage = messageDb.isMeMessage,
                        senderFullName = messageDb.senderFullName,
                        timestamp = messageDb.timestamp,
                        streamID = messageDb.streamID,
                        reactions = chatDao.getReaction(messageDb.id).map { reactionDb ->
                            Reaction(
                                reactionDb.emojiCode,
                                reactionDb.emojiName,
                                reactionDb.reactionType,
                                reactionDb.userId
                            )
                        }
                    )
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isEmpty()) {
                    shimmer?.isVisible = true
                    shimmer?.startShimmer()
                    recyclerView?.isVisible = false
                    errorView?.isVisible = false
                } else {
                    databaseIsNotEmpty = true
                    val oldSize = adapter.itemCount
                    adapter.updateData(it)
//                    recyclerView?.scrollToPosition(if (it.size > oldSize) (it.size - oldSize - 1) else oldSize - 1)
                    recyclerView?.scrollToPosition(it.size - 1)

                    isLoading = false
                }
                if (!databaseIsRefresh) getMessages(view, "newest")
            }, {
            })
            .addTo(compositeDisposable)
    }

    private fun getOldMessageFromDb(){
        val chatDao = ChatDatabase.getDatabase(requireContext()).chatDao()
        oldMessageSubscriber = chatDao.getOldMessages(nameTopic!!, firstMessageId.toLong())
            .map {
                 it.map { messageDb ->
                     UserMessage(
                         avatarURL = messageDb.avatarURL,
                         content = messageDb.content,
                         id = messageDb.id,
                         isMeMessage = messageDb.isMeMessage,
                         senderFullName = messageDb.senderFullName,
                         timestamp = messageDb.timestamp,
                         streamID = messageDb.streamID,
                         reactions = chatDao.getReaction(messageDb.id).map { reactionDb ->
                             Reaction(
                                 reactionDb.emojiCode,
                                 reactionDb.emojiName,
                                 reactionDb.reactionType,
                                 reactionDb.userId
                             )
                         }
                     )
                 }
             }
            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                adapter.addData(it)
//            },{
//
//            })
//            .addTo(compositeDisposable)

    }

    private fun getMessages(view: View, anchor: String) {
        val chatDao = ChatDatabase.getDatabase(requireContext()).chatDao()
        val chatApi = RetrofitModule.chatApi
        chatApi.getMessages(
            anchor = anchor,
            num_after = 0,
            num_before = 20,
            narrow = "[{\"operand\":\"$nameStream\", \"operator\":\"stream\"},{\"operand\":\"$nameTopic\",\"operator\":\"topic\"}]"
        )
            .subscribeOn(Schedulers.io())
            .map {
                if(it.foundOldest){
                    foundOldest = it.foundOldest
                    addToSharedpref(foundOldest)
                }
                it.messages.map { userMessage ->
                    chatDao.insertReaction(
                        userMessage.reactions.map { reaction ->
                            ReactionDb(
                                reaction.emojiCode,
                                reaction.emojiName,
                                reaction.reactionType,
                                reaction.userId,
                                userMessage.id
                            )
                        })

                    MessageDb(
                        avatarURL = userMessage.avatarURL,
                        content = userMessage.content,
                        id = userMessage.id,
                        isMeMessage = userMessage.isMeMessage,
                        senderFullName = userMessage.senderFullName,
                        timestamp = userMessage.timestamp,
                        streamID = userMessage.streamID,
                        nameTopic = nameTopic!!
                    )
                }
            }
            .doOnSuccess {
                databaseIsRefresh = true
                databaseIsNotEmpty = true
                chatDao.insertMessages(it)
                firstMessageId = it.first().id.toString()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                if (!databaseIsNotEmpty) {
                    shimmer?.isVisible = true
                    shimmer?.startShimmer()
                    recyclerView?.isVisible = false
                    errorView?.isVisible = false
                }
                isLoading = true
            }
            .doOnTerminate {
                shimmer?.stopShimmer()
                shimmer?.isVisible = false
            }
            .subscribe({
                recyclerView?.isVisible = true
                errorView?.isVisible = false
                if(isLoading) oldMessageSubscriber?.subscribe{
                    adapter.addData(it)
                    isLoading = false
                }?.addTo(compositeDisposable)

            }, {
                if (databaseIsNotEmpty) {
                    Snackbar.make(
                        view,
                        "Неудалось обновить данные",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    errorView?.isVisible = true
                    recyclerView?.isVisible = false
                }
                isLoading = false
            })
            .addTo(compositeDisposable)
    }

    private fun getMessage(messageId: Long, position: Int) {
        val chatApi = RetrofitModule.chatApi
        chatApi.getMessages(
            "newest",
            1,
            1,
            "[{\"operand\":\"$nameStream\", \"operator\":\"stream\"},{\"operand\":\"$nameTopic\",\"operator\":\"topic\"},{\"operand\":\"$messageId\",\"operator\":\"id\"}]"
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (position == SEND_MESSAGE_POSITION) {
                    updateRecycler(it.messages[0])
                } else {
                    updateRecycler(it.messages[0], position)
                }
            }, {
            })
            .addTo(compositeDisposable)
    }

    private fun onClickButtonSendMessage() {
        if (editTextMessage.text.isNotEmpty()) {
            sendMessage(message = editTextMessage.text.toString())
        }
    }


    private fun sendMessage(message: String) {
        val disposable =
            RetrofitModule.chatApi.sendMessage("stream", nameStream!!, nameTopic!!, message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        editTextMessage.text.clear()
                        getMessage(it.id, SEND_MESSAGE_POSITION)
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

    override fun itemLongClicked(idMessage: Long, position: Int): Boolean {
        showBottomSheetDialog(idMessage, position)
        return true
    }


    private fun showBottomSheetDialog(idMessage: Long, positionMessage: Int) {
        val bottomSheet = layoutInflater.inflate(R.layout.bottom_sheet, null)
        dialog = BottomSheetDialog(this.requireContext(), R.style.BottomSheetDialogTheme)
        dialog.setContentView(bottomSheet)


        val recyclerBottomSheet = bottomSheet.findViewById<RecyclerView>(R.id.bottom_sheet_recycler)
        val adapterBottomSheet =
            BottomSheetAdapter(idMessage, positionMessage) { emoji, id, position ->
                updateEmoji(emoji, id, position)
                dialog.dismiss()
            }
        recyclerBottomSheet.adapter = adapterBottomSheet
        dialog.show()
    }

    private fun updateRecycler(message: UserMessage) {
        adapter.updateMessage(message)
        recyclerView?.smoothScrollToPosition(adapter.itemCount - 1)
    }

    private fun updateRecycler(message: UserMessage, position: Int) {
        adapter.updateListEmoji(message, position)
    }


    private fun updateEmoji(emoji: String, idMessage: Long, position: Int) {
        val emojiName = Emoji.values().find {
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
        compositeDisposable.add(disposable)
    }

    private fun addToSharedpref(foundOldest: Boolean){
        val pref: SharedPreferences = requireContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = pref.edit()

        editor.putBoolean(FOUND_OLDEST_KEY, foundOldest)
        editor.apply()
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
        const val SEND_MESSAGE_POSITION = -1
        const val SHARED_PREF_NAME = "CHAT_SHARED_PREF"
        const val FOUND_OLDEST_KEY = "FOUND_OLDEST_KEY"
        const val FIRST_POSITION = 1
        const val POSITION_FOR_LOAD = 5

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