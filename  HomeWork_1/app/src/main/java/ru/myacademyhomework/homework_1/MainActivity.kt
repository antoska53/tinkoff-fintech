package ru.myacademyhomework.homework_1

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import ru.myacademyhomework.homework_1.databinding.ActivityMainBinding
import java.lang.reflect.Array

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val secondForActivityResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val contactsIntent = result.data
                    if (contactsIntent != null) {
                        val contacts =
                            contactsIntent.getStringArrayListExtra(SecondActivity.CONTACTS)
                        if (contacts != null) {
                            val adapter =
                                ArrayAdapter(this, android.R.layout.simple_list_item_1, contacts)
                            binding.contactList.adapter = adapter
                        }
                    }
                }
            }

        binding.butToActivity2.setOnClickListener {
            secondForActivityResult.launch(Intent(this, SecondActivity::class.java))
        }
    }

}