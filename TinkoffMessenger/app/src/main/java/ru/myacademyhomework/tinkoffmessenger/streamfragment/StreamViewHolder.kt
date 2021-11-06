package ru.myacademyhomework.tinkoffmessenger.streamfragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.StreamListener
import ru.myacademyhomework.tinkoffmessenger.data.ItemStream

class StreamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val nameStream: TextView = itemView.findViewById(R.id.textview_name_stream)

    fun onBind(stream: ItemStream, listener: StreamListener){
        nameStream.text = stream.nameStream
        nameStream.setOnClickListener {
            listener.onClickStream(stream)
        }
    }

    companion object{
        fun createStreamViewHolder(parent: ViewGroup): StreamViewHolder {
            return StreamViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_holder_stream, parent, false)
            )
        }
    }
}