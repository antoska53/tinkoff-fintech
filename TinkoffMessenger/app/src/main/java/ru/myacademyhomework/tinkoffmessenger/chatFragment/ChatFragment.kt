package ru.myacademyhomework.tinkoffmessenger.chatFragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.myacademyhomework.tinkoffmessenger.AppDelegate
import ru.myacademyhomework.tinkoffmessenger.listeners.ChatMessageListener
import ru.myacademyhomework.tinkoffmessenger.database.ChatDatabase
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.chatFragment.bottomsheet.BottomSheetAdapter
import ru.myacademyhomework.tinkoffmessenger.network.User
import ru.myacademyhomework.tinkoffmessenger.network.UserMessage
import javax.inject.Inject
import javax.inject.Provider


class ChatFragment: MvpAppCompatFragment(R.layout.fragment_chat), ChatMessageListener, ChatView {

    private val nameStream by lazy {
        arguments?.getString(NAME_CHANNEL) ?: ""
    }
    private val nameTopic by lazy {
        arguments?.getString(NAME_TOPIC) ?: ""
    }

    private lateinit var adapter: ChatAdapter
    private var recyclerView: RecyclerView? = null
    private lateinit var editTextMessage: EditText
    private lateinit var buttonSendMessage: ImageButton
    private lateinit var dialog: BottomSheetDialog
    private var errorView: View? = null
    private var shimmer: ShimmerFrameLayout? = null
    private var isInitRecycler = false
    private var foundOldest = false

    @Inject
    lateinit var presenterProvider: Provider<ChatPresenter>
    private val chatPresenter by moxyPresenter {
        presenterProvider.get()
    }

//    @InjectPresenter
//    lateinit var chatPresenter: ChatPresenter
//
//    @Inject
//    lateinit var presenterProvider : Provider<ChatPresenter>
//
//    @ProvidePresenter
//    fun providePresenter(): ChatPresenter {
//        return presenterProvider.get()
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppDelegate.appComponent.inject(this)
        chatPresenter.load(nameStream, nameTopic, foundOldest)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        errorView = view.findViewById(R.id.error_view)
        shimmer = view.findViewById(R.id.shimmer_chat_layout)
        val sharedPref =
            requireContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val foundOldest = sharedPref.getBoolean(FOUND_OLDEST_KEY, false)
        chatPresenter.loadFoundOldest(foundOldest)
        recyclerView = view.findViewById(R.id.chat_recycler)
        chatPresenter.initChat()

        val tvNameTopic = view.findViewById<TextView>(R.id.textview_name_topic)
        tvNameTopic.text = getString(R.string.topic, nameTopic)
        val tvNameChannel = view.findViewById<TextView>(R.id.textview_name_channel)
        tvNameChannel.text = nameStream
        val buttonReload = view.findViewById<Button>(R.id.button_reload)
        buttonReload.setOnClickListener {
            chatPresenter.buttonReloadClick()

        }

        val buttonBack = view.findViewById<ImageView>(R.id.imageView_arrow_back)
        buttonBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        buttonSendMessage = view.findViewById(R.id.button_send_message)
        buttonSendMessage.setOnClickListener { chatPresenter.onClickButtonSendMessage(editTextMessage.text) }
        editTextMessage = view.findViewById(R.id.edittext_message)
        editTextMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

            override fun afterTextChanged(s: Editable) {
                chatPresenter.buttonSendMessageSetImage(s.toString())
            }
        })
    }

    override fun onDestroyView() {
        recyclerView?.adapter = null
        super.onDestroyView()
    }

    override fun initRecycler(listUser: List<User>) {
        adapter = ChatAdapter(this, listUser[0].userID)
        recyclerView?.adapter = adapter
        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                chatPresenter.pagingChat(recyclerView)
            }
        })
    }

    override fun buttonSendMessageSetImage(resId: Int){
        buttonSendMessage.setImageResource(resId)
    }


    override fun itemLongClicked(idMessage: Long, position: Int): Boolean {
        showBottomSheetDialog(idMessage, position)
        return true
    }

    private fun showBottomSheetDialog(idMessage: Long, positionMessage: Int) {
        val bottomSheet = layoutInflater.inflate(R.layout.bottom_sheet, null)
        dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        dialog.setContentView(bottomSheet)

        val recyclerBottomSheet = bottomSheet.findViewById<RecyclerView>(R.id.bottom_sheet_recycler)
        val adapterBottomSheet =
            BottomSheetAdapter(idMessage, positionMessage) { emoji, id, position ->
                chatPresenter.updateEmoji(emoji, id, position)
                dialog.dismiss()
            }
        recyclerBottomSheet.adapter = adapterBottomSheet
        dialog.show()
    }

    override fun updateMessage(message: UserMessage) {
        adapter.updateMessage(message)
        recyclerView?.smoothScrollToPosition(adapter.itemCount - 1)
    }

    override fun updateMessage(message: UserMessage, position: Int) {
        adapter.updateListEmoji(message, position)
    }

    override fun addToSharedpref(foundOldest: Boolean) {
        val pref: SharedPreferences =
            requireContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = pref.edit()

        editor.putBoolean(FOUND_OLDEST_KEY, foundOldest)
        editor.apply()
    }

    override fun updateRecyclerData(listUserMessage: List<UserMessage>) {
        adapter.updateData(listUserMessage)
        val chatDiffUtilCallback = ChatDiffUtilCallback(adapter.messages, listUserMessage)
        val chatDiffResult = DiffUtil.calculateDiff(chatDiffUtilCallback)
        chatDiffResult.dispatchUpdatesTo(adapter)
        recyclerView?.scrollToPosition(listUserMessage.size - 1)
    }

    override fun addRecyclerData(listUserMessage: List<UserMessage>) {
        adapter.addData(listUserMessage)
    }

    override fun showRefresh() {
        shimmer?.isVisible = true
        shimmer?.startShimmer()
        recyclerView?.isVisible = false
        errorView?.isVisible = false
    }

    override fun hideRefresh() {
        shimmer?.stopShimmer()
        shimmer?.isVisible = false
    }

    override fun showRecycler() {
        recyclerView?.isVisible = true
        errorView?.isVisible = false
    }

    override fun showErrorUpdateData() {
        Snackbar.make(
            requireView(),
            "Неудалось обновить данные",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun showErrorSendMessage() {
        Snackbar.make(
            requireView(),
            "Сообщение не отправлено",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun showError() {
        errorView?.isVisible = true
    }

    override fun clearEditText() {
        editTextMessage.text.clear()
    }

    override fun showErrorUpdateEmoji() {
        Snackbar.make(
            requireView(),
            "Неудалось добавить эмодзи \uD83D\uDE2D",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun hideError() {
        errorView?.isVisible = false
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