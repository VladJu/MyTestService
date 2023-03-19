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
            //2) Чтобы остановить снаружи (к приему по нажатию какой нибудь кнопки)
            stopService(MyForegroundService.newIntent(this))
            startService(MyService.newIntent(this, 10))
        }
        binding.foregroundService.setOnClickListener {
            ContextCompat.startForegroundService(this,
                MyForegroundService.newIntent(this))
        }
    }

}