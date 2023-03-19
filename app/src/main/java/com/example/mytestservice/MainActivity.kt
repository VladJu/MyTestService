package com.example.mytestservice

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mytestservice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.simpleService.setOnClickListener {
            stopService(MyForegroundService.newIntent(this))
            startService(MyService.newIntent(this, 10))
        }
        binding.foregroundService.setOnClickListener {
            ContextCompat.startForegroundService(this,
                MyForegroundService.newIntent(this))
        }
        //2
        binding.intentService.setOnClickListener {
            ContextCompat.startForegroundService(this,
                MyIntentService.newIntent(this))
        }
        //запускать либо как обычный сервис
        //startService(MyIntentService.newIntent(this))
    }
}