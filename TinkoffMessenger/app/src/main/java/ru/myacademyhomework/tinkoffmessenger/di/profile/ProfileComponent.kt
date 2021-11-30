package ru.myacademyhomework.tinkoffmessenger.di.profile

import dagger.Subcomponent
import ru.myacademyhomework.tinkoffmessenger.profilefragment.ProfileFragment

@ProfileScope
@Subcomponent(modules = [ProfileModule::class])
interface ProfileComponent {
    fun inject(profileFragment: ProfileFragment)
}