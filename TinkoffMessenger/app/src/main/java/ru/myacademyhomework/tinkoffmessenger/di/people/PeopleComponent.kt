package ru.myacademyhomework.tinkoffmessenger.di.people

import dagger.Subcomponent
import ru.myacademyhomework.tinkoffmessenger.peoplefragment.PeopleFragment

@PeopleScope
@Subcomponent()
interface PeopleComponent {
    fun inject(peopleFragment: PeopleFragment)
}