package ru.myacademyhomework.homework_1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.myacademyhomework.homework_1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ContactAdapter
    private lateinit var secondForActivityResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecycler()

        secondForActivityResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                updateData(result)
            }

        binding.secondActivityButton.setOnClickListener {
            secondForActivityResult.launch(Intent(this, SecondActivity::class.java))
        }
    }

    private fun initRecycler() {
        adapter = ContactAdapter()
        binding.recycler.adapter = adapter
    }

    private fun updateData(result: ActivityResult) {
        result.data?.let { intent ->
            intent.getStringArrayListExtra(SecondActivity.CONTACTS)?.let { arrayList ->
                adapter.updateContacts(arrayList)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        secondForActivityResult.unregister()
    }
}