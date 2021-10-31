package ru.myacademyhomework.tinkoffmessenger.streamfragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.myacademyhomework.tinkoffmessenger.R

class StreamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val nameStream: TextView = itemView.findViewById(R.id.textview_name_stream)

    fun onBind(name: String){
        nameStream.text = name
        nameStream.setOnClickListener {
            listener.onClick(name)
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