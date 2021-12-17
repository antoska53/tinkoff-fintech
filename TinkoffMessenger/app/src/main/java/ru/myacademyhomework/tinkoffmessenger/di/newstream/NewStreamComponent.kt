package ru.myacademyhomework.tinkoffmessenger.di.newstream

import dagger.Subcomponent
import ru.myacademyhomework.tinkoffmessenger.streamfragment.newstreamfragment.NewStreamFragment

@NewStreamScope
@Subcomponent
interface NewStreamComponent {
    fun inject(newStreamFragment: NewStreamFragment)
}