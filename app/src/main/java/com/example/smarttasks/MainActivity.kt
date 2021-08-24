package com.example.smarttasks

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smarttasks.databinding.ActivityMainBinding
import com.example.smarttasks.task.ui.fragments.TaskFragment

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFragment()
    }

    private fun setFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container_view, TaskFragment())
            .commit()
    }
}