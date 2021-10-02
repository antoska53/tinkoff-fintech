package ru.myacademyhomework.homework_1

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Instrumentation
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import ru.myacademyhomework.homework_1.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private lateinit var br: BroadcastReceiver
    private lateinit var binding: ActivitySecondBinding
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private var isRationaleShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.butService.setOnClickListener { onClickStartService() }

        br = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                setResult(Activity.RESULT_OK, intent)
                stopService(Intent(context, UsefulService::class.java))
                finish()
            }
        }

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    startService(Intent(this, UsefulService::class.java))
                }
            }
    }

    private fun onClickStartService() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED -> {
                startService(Intent(this, UsefulService::class.java))
            }
            shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                showContactsPermissionDialog()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
        }
    }

    private fun showContactsPermissionDialog() {
        AlertDialog.Builder(this)
            .setMessage("доступ к контактам нужен для запуска сервиса")
            .setPositiveButton("OK") { dialog, _ ->
                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                isRationaleShown = true
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter(BROADCAST_ACTION)
        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        localBroadcastManager.registerReceiver(br, intentFilter)
    }

    override fun onStop() {
        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        localBroadcastManager.unregisterReceiver(br)
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        requestPermissionLauncher.unregister()
    }

    companion object {
        const val BROADCAST_ACTION = "ru.myacademyhomework.homework_1"
        const val CONTACTS = "CONTACTS"
    }
}