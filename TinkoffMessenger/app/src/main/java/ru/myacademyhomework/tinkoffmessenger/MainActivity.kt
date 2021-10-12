package ru.myacademyhomework.tinkoffmessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ru.myacademyhomework.tinkoffmessenger.MessageView.EmojiView
import ru.myacademyhomework.tinkoffmessenger.MessageView.FlexBoxLayout
import ru.myacademyhomework.tinkoffmessenger.MessageView.MessageViewGroup

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val messageViewGroup = findViewById<MessageViewGroup>(R.id.message_view_group)
        for (i in 0 until messageViewGroup.childCount) {
            val child = messageViewGroup.getChildAt(i)
            if (child is FlexBoxLayout) {
                for (j in 0 until child.childCount) {
                    val childEmoji = child.getChildAt(j)
                    if (childEmoji is EmojiView) {
                        childEmoji.setOnClickListener {
                            childEmoji.isSelected = !it.isSelected
                            if (childEmoji.isSelected) {
                                childEmoji.textCount = "2"
                                childEmoji.smile = "\uD83D\uDE06"
                            } else {
                                childEmoji.textCount = "1"
                                childEmoji.smile = "\uD83D\uDE05"
                            }
                        }
                    }
                }
            }
        }
    }
}