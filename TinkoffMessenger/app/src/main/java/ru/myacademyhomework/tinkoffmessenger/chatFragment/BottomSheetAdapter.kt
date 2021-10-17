package ru.myacademyhomework.tinkoffmessenger.chatFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.myacademyhomework.tinkoffmessenger.BottomSheetListener
import ru.myacademyhomework.tinkoffmessenger.R

class BottomSheetAdapter(private val listener: BottomSheetListener) : RecyclerView.Adapter<BottomSheetViewHolder>() {
    private val emojiList: List<String> = listOf(
        "\uD83D\uDE00",
        "\uD83D\uDE03",
        "\uD83D\uDE04",
        "\uD83D\uDE01",
        "\uD83D\uDE06",
        "\uD83D\uDE05",
        "\uD83E\uDD23",
        "\uD83D\uDE02",
        "\uD83D\uDE42",
        "\uD83D\uDE43"
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetViewHolder {
        return BottomSheetViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_holder_bottomsheet, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        holder.onBind(emojiList[position], listener)
    }

    override fun getItemCount(): Int = emojiList.size

}