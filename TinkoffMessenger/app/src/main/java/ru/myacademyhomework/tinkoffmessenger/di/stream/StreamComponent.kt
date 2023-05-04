package ru.myacademyhomework.tinkoffmessenger.di.stream

import dagger.Subcomponent
import ru.myacademyhomework.tinkoffmessenger.presentation.streamfragment.StreamFragment

@StreamScope
@Subcomponent(modules = [StreamModule::class])
interface StreamComponent {
    fun inject(streamFragment: StreamFragment)
}