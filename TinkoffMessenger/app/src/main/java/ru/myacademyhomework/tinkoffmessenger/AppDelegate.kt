package ru.myacademyhomework.tinkoffmessenger

import android.app.Application
import ru.myacademyhomework.tinkoffmessenger.di.AppComponent
import ru.myacademyhomework.tinkoffmessenger.di.DaggerAppComponent
import ru.myacademyhomework.tinkoffmessenger.di.StorageModule

class AppDelegate : Application() {

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder().storageModule(StorageModule(this)).build()
    }

    companion object {
        lateinit var appComponent: AppComponent
    }
}