package com.example.wearther

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.wearther.databinding.ActivityMainBinding
import com.example.wearther.fragments.MainFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction().replace(R.id.place_holder, MainFragment.newInstance()).commit()
    }
}