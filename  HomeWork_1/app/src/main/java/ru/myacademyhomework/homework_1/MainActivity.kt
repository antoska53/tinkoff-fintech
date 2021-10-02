package ru.myacademyhomework.homework_1

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.myacademyhomework.homework_1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ContactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecycler()

        val secondForActivityResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val contactsIntent = result.data
                    if (contactsIntent != null) {
                        val contacts =
                            contactsIntent.getStringArrayListExtra(SecondActivity.CONTACTS)
                        if (contacts != null) {
                            adapter.updateContacts(contacts)
                        }
                    }
                }
            }

        binding.butToActivity2.setOnClickListener {
            secondForActivityResult.launch(Intent(this, SecondActivity::class.java))
        }
    }

    private fun initRecycler(){
        adapter = ContactAdapter()
        binding.recycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.recycler.adapter = adapter
    }

}