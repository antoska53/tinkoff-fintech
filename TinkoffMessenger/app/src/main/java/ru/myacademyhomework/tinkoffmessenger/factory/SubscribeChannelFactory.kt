package ru.myacademyhomework.tinkoffmessenger.factory

import ru.myacademyhomework.tinkoffmessenger.data.ItemChannel
import ru.myacademyhomework.tinkoffmessenger.data.ItemStream

class SubscribeChannelFactory {
    companion object {
        val channels: MutableList<Any> = mutableListOf(
            ItemChannel(
                nameChannel = "#general",
                streams = listOf(
                    ItemStream("#general", "Testing"),
                    ItemStream("#general", "Bruh")
                )
            ),
            ItemChannel(
                nameChannel = "#Development",
                streams = listOf(
                    ItemStream("#Development", "Testing2"),
                    ItemStream("#Development", "Bruhh"),
                    ItemStream("#Development", "Bruhbruh")
                )
            ),
            ItemChannel(
                nameChannel = "#Design",
                streams = listOf(
                    ItemStream("#Design", "Testing3"),
                    ItemStream("#Design", "BruhDesign")
                )
            )
        )
    }
}