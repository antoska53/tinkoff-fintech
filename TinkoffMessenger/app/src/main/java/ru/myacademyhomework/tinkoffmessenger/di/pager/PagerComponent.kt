package ru.myacademyhomework.tinkoffmessenger.di.pager

import dagger.Subcomponent
import ru.myacademyhomework.tinkoffmessenger.presentation.streamfragment.pagerfragment.AllStreamFragment
import ru.myacademyhomework.tinkoffmessenger.presentation.streamfragment.pagerfragment.SubscribedFragment

@PagerScope
@Subcomponent()
interface PagerComponent {
    fun inject(subscribedFragment: SubscribedFragment)
    fun inject(allStreamFragment: AllStreamFragment)
}