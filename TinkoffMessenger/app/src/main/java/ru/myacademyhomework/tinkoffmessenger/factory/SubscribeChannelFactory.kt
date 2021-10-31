package ru.myacademyhomework.tinkoffmessenger.factory

import ru.myacademyhomework.tinkoffmessenger.data.ItemChannel

class SubscribeChannelFactory {
    companion object{
        val channels: MutableList<Any> = mutableListOf(
            ItemChannel(nameChannel = "#general", streams = listOf("Testing", "Bruh")),
            ItemChannel(nameChannel = "#Development", streams = listOf("Testing2", "Bruhh","Bruhbruh")),
            ItemChannel(nameChannel = "#Design", streams = listOf("Testing3", "BruhDesign")),
            ItemChannel(nameChannel = "#PR", streams = listOf("Testing4", "BruhPR")),
        )
    }
}