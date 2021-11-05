package ru.myacademyhomework.tinkoffmessenger.factory

import ru.myacademyhomework.tinkoffmessenger.data.Item
import ru.myacademyhomework.tinkoffmessenger.data.ItemChannel
import ru.myacademyhomework.tinkoffmessenger.data.ItemStream

class ChannelFactory {
    companion object {
        private val channels: MutableList<Item> = mutableListOf(
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
        fun createChannel(): MutableList<Item> {
            return channels
        }
    }
}