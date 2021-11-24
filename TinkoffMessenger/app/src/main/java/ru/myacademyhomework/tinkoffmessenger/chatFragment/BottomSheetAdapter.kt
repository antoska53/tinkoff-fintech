package ru.myacademyhomework.tinkoffmessenger.chatFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.myacademyhomework.tinkoffmessenger.BottomSheetListener
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.factory.SmileFactory

class BottomSheetAdapter(
    private val idMessage: Long,
    private val positionMessage: Int,
    private val listener: BottomSheetListener
) :
    RecyclerView.Adapter<BottomSheetViewHolder>() {
    private val emojiList: List<String> = SmileFactory.createSmile()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetViewHolder {
        return BottomSheetViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_holder_bottomsheet, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        holder.onBind(
            emoji = emojiList[position],
            idMessage = idMessage,
            positionMessage = positionMessage,
            listener = listener
        )
    }

    override fun getItemCount(): Int = emojiList.size

}
