package ru.myacademyhomework.tinkoffmessenger.di.people

import dagger.Subcomponent
import ru.myacademyhomework.tinkoffmessenger.peoplefragment.PeopleFragment

@PeopleScope
@Subcomponent(modules = [PeopleModule::class])
interface PeopleComponent {
    fun inject(peopleFragment: PeopleFragment)
}