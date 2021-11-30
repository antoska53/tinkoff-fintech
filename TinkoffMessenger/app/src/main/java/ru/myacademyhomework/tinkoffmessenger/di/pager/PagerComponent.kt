package ru.myacademyhomework.tinkoffmessenger.di.pager

import dagger.Subcomponent
import ru.myacademyhomework.tinkoffmessenger.streamfragment.pagerfragments.AllStreamFragment
import ru.myacademyhomework.tinkoffmessenger.streamfragment.pagerfragments.SubscribedFragment

@PagerScope
@Subcomponent(modules = [PagerModule::class])
interface PagerComponent {
    fun inject(subscribedFragment: SubscribedFragment)
    fun inject(allStreamFragment: AllStreamFragment)
}