package ru.myacademyhomework.homework_1

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.provider.ContactsContract
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class UsefulService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        getContact()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun getContact() {
        val contacts = arrayListOf<String>()
        val cursor =
            contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        while (cursor?.moveToNext() == true) {
            contacts.add(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)))
        }
        cursor?.close()
        sendLocalBroadcast(contacts)

    }

    private fun sendLocalBroadcast(contacts: ArrayList<String>) {
        val intent =
            Intent(SecondActivity.BROADCAST_ACTION).putExtra(SecondActivity.CONTACTS, contacts)
        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        localBroadcastManager.sendBroadcast(intent)
    }

}