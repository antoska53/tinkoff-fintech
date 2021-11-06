package ru.myacademyhomework.tinkoffmessenger.factory

class PeopleFactory {
    companion object {
        private val listPeople: List<String> = listOf("Hello World", "Qwerty Wasd")

        fun createPeople(): List<String> {
            return listPeople
        }
    }
}