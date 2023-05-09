package ru.myacademyhomework.tinkoffmessenger.di.newstream

import dagger.Subcomponent
import ru.myacademyhomework.tinkoffmessenger.presentation.streamfragment.newstreamfragment.NewStreamFragment

@NewStreamScope
@Subcomponent(modules = [NewStreamModule::class])
interface NewStreamComponent {
    fun inject(newStreamFragment: NewStreamFragment)
}