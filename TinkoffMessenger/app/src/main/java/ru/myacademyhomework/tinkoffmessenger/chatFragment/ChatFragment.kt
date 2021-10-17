package ru.myacademyhomework.tinkoffmessenger.chatFragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import ru.myacademyhomework.tinkoffmessenger.BottomSheetListener
import ru.myacademyhomework.tinkoffmessenger.ChatMessageListener
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.data.Message


class ChatFragment : Fragment(), ChatMessageListener, BottomSheetListener {
    private lateinit var adapter: ChatAdapter
    private lateinit var adapterBottomSheet: BottomSheetAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var editTextMessage: EditText
    private lateinit var buttonSendMessage: ImageButton
    private lateinit var dialog: BottomSheetDialog
    private var positionMessage: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler(view)
        initBottomSheetDialog()
        buttonSendMessage = view.findViewById(R.id.button_send_message)
        buttonSendMessage.setOnClickListener { onClickButtonSendMessage() }
        editTextMessage = view.findViewById(R.id.edittext_message)
        editTextMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (it.toString().isNotEmpty()) {
                        buttonSendMessage.setImageResource(R.drawable.plane)
                    } else {
                        buttonSendMessage.setImageResource(R.drawable.cross)
                    }
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    private fun initRecycler(view: View) {
        recyclerView = view.findViewById(R.id.chat_recycler)
        adapter = ChatAdapter(this)
        recyclerView.adapter = adapter
    }

    private fun onClickButtonSendMessage() {
        if (editTextMessage.text.isNotEmpty()) {
            val message = Message(
                avatar = "",
                name = "Me",
                message = editTextMessage.text.toString(),
                listEmoji = mutableListOf()
            )
            updateRecycler(message)
        }
    }

    override fun itemLongClicked(position: Int): Boolean {
        positionMessage = position
        Log.d("ID", "itemLongClicked: $positionMessage")
        return showBottomSheetDialog()
    }

    private fun initBottomSheetDialog() {
        val bottomSheet = layoutInflater.inflate(R.layout.bottom_sheet, null)
        dialog = BottomSheetDialog(this.requireContext())
        dialog.setContentView(bottomSheet)
        bottomSheet.setOnClickListener {
            dialog.dismiss()
        }

        val recyclerBottomSheet = bottomSheet.findViewById<RecyclerView>(R.id.bottom_sheet_recycler)
        adapterBottomSheet = BottomSheetAdapter(this)
        recyclerBottomSheet.adapter = adapterBottomSheet

    }

    override fun itemEmojiClicked(emoji: String) {
        updateEmoji(emoji)
        // val emojiView = EmojiView(requireContext())
        //  emojiView.smile = emoji
        //  requireActivity().findViewById<FlexBoxLayout>(flexBoxId).addView(emojiView)
    }

    private fun showBottomSheetDialog(): Boolean {
        dialog.show()
        return true
    }

    private fun updateRecycler(message: Message) {
        adapter.updateData(message)
    }

    private fun updateEmoji(emoji: String) {
        adapter.updateListEmoji(emoji, positionMessage)
    }

    companion object {
        const val TYPE_DATE = 0

        const val TYPE_MESSAGE = 1

        @JvmStatic
        fun newInstance() = ChatFragment()
    }
}