package ru.myacademyhomework.tinkoffmessenger.presentation

import android.app.Application
import ru.myacademyhomework.tinkoffmessenger.di.AppComponent
import ru.myacademyhomework.tinkoffmessenger.di.DaggerAppComponent
import ru.myacademyhomework.tinkoffmessenger.di.StorageModule

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder().storageModule(StorageModule(applicationContext)).build()
    }
}