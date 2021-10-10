package ru.myacademyhomework.tinkoffmessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.myacademyhomework.tinkoffmessenger.MessageView.EmojiView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val emojiView = findViewById<EmojiView>(R.id.emoji_view)
//        emojiView.setOnClickListener {
//            emojiView.isSelected = !it.isSelected
//            if(emojiView.isSelected){
//                emojiView.textCount = "2"
//                emojiView.smile = "\uD83D\uDE06"
//            }else{
//                emojiView.textCount = "100500"
//                emojiView.smile = "\uD83D\uDE05"
//            }
//        }

    }
}