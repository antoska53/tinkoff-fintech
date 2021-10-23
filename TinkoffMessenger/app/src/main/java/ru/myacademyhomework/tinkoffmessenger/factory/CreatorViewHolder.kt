package ru.myacademyhomework.tinkoffmessenger.factory

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class CreatorViewHolder {
    abstract fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
}