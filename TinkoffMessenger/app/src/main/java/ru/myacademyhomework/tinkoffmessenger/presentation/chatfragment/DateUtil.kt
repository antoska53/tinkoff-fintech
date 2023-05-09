package ru.myacademyhomework.tinkoffmessenger.presentation.chatfragment

import java.util.*

class DateUtil {
    companion object {
        fun isSameDay(date1: Date, date2: Date): Boolean {
            val calendar: Calendar = Calendar.getInstance()
            val calendar2: Calendar = Calendar.getInstance()
            calendar.time = date1
            calendar2.time = date2
            return calendar.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                    calendar.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
        }
    }
}