package com.example.kotlin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlin.R
import com.example.kotlin.databinding.ActivityMainWebViewBinding
import com.example.kotlin.view.weatherlist.WeatherListFragment

class MainActivityWebView : AppCompatActivity() {
    lateinit var binding: ActivityMainWebViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ok.setOnClickListener {

        }

    }
}