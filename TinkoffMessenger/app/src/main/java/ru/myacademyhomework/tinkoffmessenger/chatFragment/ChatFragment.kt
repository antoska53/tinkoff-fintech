package ru.myacademyhomework.tinkoffmessenger.chatFragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import ru.myacademyhomework.tinkoffmessenger.App
import ru.myacademyhomework.tinkoffmessenger.FlowFragment
import ru.myacademyhomework.tinkoffmessenger.FragmentNavigation
import ru.myacademyhomework.tinkoffmessenger.listeners.ChatMessageListener
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.chatFragment.bottomsheet.BottomSheetActionAdapter
import ru.myacademyhomework.tinkoffmessenger.chatFragment.bottomsheet.BottomSheetAdapter
import ru.myacademyhomework.tinkoffmessenger.data.ChatMessage
import ru.myacademyhomework.tinkoffmessenger.database.TopicDb
import ru.myacademyhomework.tinkoffmessenger.network.User
import ru.myacademyhomework.tinkoffmessenger.network.UserMessage
import javax.inject.Inject
import javax.inject.Provider


class ChatFragment : MvpAppCompatFragment(R.layout.fragment_chat), ChatMessageListener, ChatView {

    private val nameStream by lazy {
        arguments?.getString(NAME_CHANNEL) ?: ""
    }
    private val nameTopic by lazy {
        arguments?.getString(NAME_TOPIC) ?: ""
    }

    private var navigation: FragmentNavigation? = null
    private lateinit var adapter: ChatAdapter
    private var recyclerView: RecyclerView? = null
    private lateinit var editTextMessage: EditText
    private lateinit var buttonSendMessage: ImageButton
    private lateinit var buttonEditMessage: ImageButton
    private lateinit var textviewTopicChat: TextView
    private lateinit var textviewNameTopic: TextView
    private var errorView: View? = null
    private var shimmer: ShimmerFrameLayout? = null
    private var firstLoadFlag = false

    @Inject
    lateinit var presenterProvider: Provider<ChatPresenter>
    private val chatPresenter by moxyPresenter {
        presenterProvider.get()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        require(context is FragmentNavigation)
        navigation = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity?.application as App).appComponent.getChatComponent().inject(this)
        super.onCreate(savedInstanceState)

        setStatusBarColor(FlowFragment.LIGHT_COLOR)

        val sharedPref =
            requireContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val foundOldest = sharedPref.getBoolean(nameStream + nameTopic, false)
        chatPresenter.loadFoundOldest(foundOldest)

        chatPresenter.load(nameStream, nameTopic, foundOldest)
        chatPresenter.initChat()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        errorView = view.findViewById(R.id.error_view)
        shimmer = view.findViewById(R.id.shimmer_chat_layout)
        textviewTopicChat = view.findViewById(R.id.textview_topic_for_message)
        textviewTopicChat.text = CHOOSE_TOPIC
        textviewTopicChat.setOnClickListener {
            chatPresenter.showPopupMenu()
        }

        recyclerView = view.findViewById(R.id.chat_recycler)

        textviewNameTopic = view.findViewById(R.id.textview_name_topic)
        textviewNameTopic.text = getString(R.string.topic, nameTopic)
        val tvNameStream = view.findViewById<TextView>(R.id.textview_name_channel)
        tvNameStream.text = nameStream
        val buttonReload = view.findViewById<Button>(R.id.button_reload)
        buttonReload.setOnClickListener {
            chatPresenter.buttonReloadClick()
        }

        val buttonBack = view.findViewById<ImageView>(R.id.imageView_arrow_back)
        buttonBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        buttonSendMessage = view.findViewById(R.id.button_send_message)
        buttonSendMessage.setOnClickListener {
            chatPresenter.onClickButtonSendMessage(
                editTextMessage.text, textviewTopicChat.text.toString()
            )
        }
        buttonEditMessage = view.findViewById(R.id.button_edit_message)
        editTextMessage = view.findViewById(R.id.edittext_message)
        editTextMessage.addTextChangedListener{ str ->
            chatPresenter.buttonSendMessageSetImage(str.toString())
        }

        chatPresenter.showNameTopic()
        chatPresenter.showChooseTopic()
    }

    override fun onDestroyView() {
        recyclerView?.adapter = null
        super.onDestroyView()
    }

    private fun setStatusBarColor(color: Int) {
        val window = activity?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.statusBarColor = resources.getColor(color, null)
    }

    override fun initRecycler(listUser: List<User>) {
        adapter = ChatAdapter(this, listUser[0].userID){nameTopic ->
            chatPresenter.onClickTopic(nameTopic)
        }
        recyclerView?.adapter = adapter
        if (nameTopic != STREAM_CHAT) {
            recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    chatPresenter.pagingChat(recyclerView)
                }
            })
        }
    }


    override fun buttonSendMessageSetImage(resId: Int) {
        buttonSendMessage.setImageResource(resId)
    }

    override fun itemLongClicked(idMessage: Long, nameTopic: String, message: String, position: Int): Boolean {
        showActionBottomSheetDialog(idMessage, nameTopic, message, position)
        return true
    }

    override fun plusButtonClicked(idMessage: Long, nameTopic: String, position: Int): Boolean {
        showEmojiBottomSheetDialog(idMessage, nameTopic, position)
        return true
    }

    override fun itemAddReactionClicked(messageId: Long, nameTopic: String, emojiName: String, position: Int) {
        chatPresenter.addReaction(messageId, nameTopic, emojiName, position)
    }

    override fun itemRemoveReactionClicked(messageId: Long, nameTopic: String, emojiName: String,
        emojiCode: String, reactionType: String, userId: Int, position: Int
    ) {
        chatPresenter.removeReaction(messageId, nameTopic, emojiName, emojiCode, reactionType,
            userId, position )
    }

    override fun showEmojiBottomSheetDialog(idMessage: Long, nameTopic: String, positionMessage: Int) {
        val bottomSheet = layoutInflater.inflate(R.layout.bottom_sheet, null)
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        dialog.setContentView(bottomSheet)

        val recyclerBottomSheet = bottomSheet.findViewById<RecyclerView>(R.id.bottom_sheet_recycler)
        val adapterBottomSheet =
            BottomSheetAdapter(idMessage, nameTopic, positionMessage) { emoji, id, nametopic, position ->
                chatPresenter.updateEmoji(emoji, id, nametopic, position)
                dialog.dismiss()
            }
        recyclerBottomSheet.adapter = adapterBottomSheet
        dialog.show()
    }

    private fun showActionBottomSheetDialog(idMessage: Long, nameTopic: String, message: String, positionMessage: Int) {
        val bottomSheet = layoutInflater.inflate(R.layout.bottom_sheet_action, null)
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        dialog.setContentView(bottomSheet)

        val recyclerBottomSheet = bottomSheet.findViewById<RecyclerView>(R.id.bottom_sheet_action_recycler)
        val adapterBottomSheet =
            BottomSheetActionAdapter(idMessage, nameTopic, message, positionMessage) {action, id, nametopic, message_for_edit, position ->
                chatPresenter.actionBottomSheet(action, id, nametopic, message_for_edit, position)
                dialog.dismiss()
            }
        recyclerBottomSheet.adapter = adapterBottomSheet
        dialog.show()
    }

    override fun showPopupMenu(listTopic: List<TopicDb>) {
        val popupMenu = PopupMenu(requireContext(), textviewTopicChat)
        listTopic.forEach {
            popupMenu.menu.add(it.nameTopic)
        }
        popupMenu.setOnMenuItemClickListener{
            textviewTopicChat.text = it.title
            true
        }
        popupMenu.show()
    }

    override fun showErrorPopupMenu() {
        showSnackbar(getString(R.string.error_popup_menu))
    }

    override fun updateMessage(message: UserMessage, isStreamChat: Boolean) {
        adapter.updateMessage(message, isStreamChat)
        recyclerView?.smoothScrollToPosition(adapter.itemCount - 1)
    }

    override fun updateMessage(message: UserMessage, position: Int) {
        adapter.updateListEmoji(message, position)
    }

    override fun addToSharedpref(foundOldest: Boolean) {
        val pref: SharedPreferences =
            requireContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = pref.edit()
        editor.putBoolean(nameStream + nameTopic, foundOldest)
        editor.apply()
    }

    override fun updateRecyclerData(listUserMessage: List<ChatMessage>) {
        val chatDiffUtilCallback = ChatDiffUtilCallback(adapter.messages, listUserMessage)
        val chatDiffResult = DiffUtil.calculateDiff(chatDiffUtilCallback)
        adapter.updateData(listUserMessage)
        chatDiffResult.dispatchUpdatesTo(adapter)
        if (!firstLoadFlag){
            recyclerView?.scrollToPosition(listUserMessage.size - 1)
            firstLoadFlag = true
        }
    }

    override fun addRecyclerData(listUserMessage: List<ChatMessage>) {
        adapter.addData(listUserMessage)
    }

    override fun openChatTopic(nameStream: String, nameTopic: String) {
        navigation?.openChatFragment(
            ChatFragment.newInstance(
                nameStream,
                nameTopic
            )
        )
    }

    override fun setupEditMessage(messageId: Long, nameTopic: String, message: String) {
        buttonEditMessage.isVisible = true
        buttonSendMessage.isVisible = false
        textviewTopicChat.isVisible = true
        editTextMessage.text.clear()
        editTextMessage.setText(message)

        buttonEditMessage.setOnClickListener {
            chatPresenter.onClickButtonEditMessage(
                messageId, editTextMessage.text, textviewTopicChat.text.toString()
            )
        }
    }

    override fun copyMessage(message: String){
        val clipboard: ClipboardManager =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("text", message)
        clipboard.setPrimaryClip(clip)
    }

    override fun destroyEditMessage() {
        buttonSendMessage.isVisible = true
        textviewTopicChat.isVisible = nameTopic == STREAM_CHAT
        buttonEditMessage.isVisible = false
        editTextMessage.text.clear()
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

    private fun showSnackbar(message: String){
        Snackbar.make(
            requireView(),
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun showErrorUpdateData() {
        showSnackbar(getString(R.string.error_update_data))
    }

    override fun showErrorSendMessage() {
        showSnackbar(getString(R.string.message_not_sent))
    }

    override fun showErrorChooseTopic() {
        showSnackbar(getString(R.string.choose_topic))
    }

    override fun showError() {
        errorView?.isVisible = true
    }

    override fun showErrorAddReaction() {
        showSnackbar(getString(R.string.error_add_emoji))
    }

    override fun showErrorRemoveReaction() {
        showSnackbar(getString(R.string.error_delete_emoji))
    }

    override fun showDeleteMessageSuccess() {
        showSnackbar(getString(R.string.message_deleted))
    }

    override fun showErrorDeleteMessage() {
        showSnackbar(getString(R.string.error_delete_message))
    }

    override fun showErrorEmptyEditMessage() {
        showSnackbar(getString(R.string.error_empty_edit_message))
    }

    override fun showErrorEditMessage() {
        showSnackbar(getString(R.string.error_edit_message))
    }

    override fun clearEditText() {
        editTextMessage.text.clear()
    }

    override fun showErrorUpdateEmoji() {
        showSnackbar(getString(R.string.error_add_emoji))
    }

    override fun hideError() {
        errorView?.isVisible = false
    }

    override fun showNameTopic(isVisible: Int){
        textviewNameTopic.visibility = isVisible
    }

    override fun showChooseTopic(isVisible: Int){
        textviewTopicChat.visibility = isVisible
    }

    companion object {
        const val TYPE_DATE = 0
        const val TYPE_MESSAGE = 1
        const val TYPE_TOPIC = 2
        const val NAME_CHANNEL = "NAME_CHANNEL"
        const val NAME_TOPIC = "NAME_STREAM"
        const val SEND_MESSAGE_POSITION = -1
        const val SHARED_PREF_NAME = "CHAT_SHARED_PREF"
        const val FIRST_POSITION = 1
        const val POSITION_FOR_LOAD = 5
        const val CHOOSE_TOPIC = "Choose topic..."
        const val STREAM_CHAT = "STREAM_CHAT"

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