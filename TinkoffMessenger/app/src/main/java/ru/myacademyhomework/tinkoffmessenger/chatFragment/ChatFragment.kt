package ru.myacademyhomework.tinkoffmessenger.chatFragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import ru.myacademyhomework.tinkoffmessenger.ChatMessageListener
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.data.Message
import ru.myacademyhomework.tinkoffmessenger.factory.MessageFactory


class ChatFragment : Fragment(R.layout.fragment_chat), ChatMessageListener {
    private lateinit var adapter: ChatAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var editTextMessage: EditText
    private lateinit var buttonSendMessage: ImageButton
    private lateinit var dialog: BottomSheetDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler(view)
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

    override fun onDestroyView() {
        recyclerView.adapter = null
        super.onDestroyView()
    }


    private fun initRecycler(view: View) {
         val recyclerView = view.findViewById<RecyclerView>(R.id.chat_recycler)
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
            editTextMessage.text.clear()
            updateRecycler(message)
        }
    }

    override fun itemLongClicked(idMessage: Int): Boolean {
        showBottomSheetDialog(idMessage)
        return true
    }


    private fun showBottomSheetDialog(idMessage: Int) {
        val bottomSheet = layoutInflater.inflate(R.layout.bottom_sheet, null)
        dialog = BottomSheetDialog(this.requireContext(), R.style.BottomSheetDialogTheme)
        dialog.setContentView(bottomSheet)
        bottomSheet.setOnClickListener {
            dialog.dismiss()
        }

        val recyclerBottomSheet = bottomSheet.findViewById<RecyclerView>(R.id.bottom_sheet_recycler)
        val adapterBottomSheet = BottomSheetAdapter(idMessage) { emoji, id ->
            updateEmoji(emoji, id)
        }
        recyclerBottomSheet.adapter = adapterBottomSheet
        dialog.show()
    }

    private fun updateRecycler(message: Message) {
        adapter.updateData(message)
    }


    private fun updateEmoji(emoji: String, idMessage: Int) {
        val message = MessageFactory.messages[idMessage]
        if (message is Message) {
            message.listEmoji.add(emoji)
            adapter.updateListEmoji(idMessage)
        }
    }

    companion object {
        const val TYPE_DATE = 0

        const val TYPE_MESSAGE = 1

        @JvmStatic
        fun newInstance() = ChatFragment()
    }
}