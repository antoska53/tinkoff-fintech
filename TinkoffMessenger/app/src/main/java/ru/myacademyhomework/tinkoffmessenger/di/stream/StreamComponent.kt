package ru.myacademyhomework.tinkoffmessenger.di.stream

import dagger.Subcomponent
import ru.myacademyhomework.tinkoffmessenger.streamfragment.StreamFragment

@StreamScope
@Subcomponent()
interface StreamComponent {
    fun inject(streamFragment: StreamFragment)
}