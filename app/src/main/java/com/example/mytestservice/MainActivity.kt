package com.example.mytestservice

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
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
        binding.intentService.setOnClickListener {
            ContextCompat.startForegroundService(this,
                MyIntentService.newIntent(this))
        }
        //1
        binding.jobScheduler.setOnClickListener {
            //2указыавем какой сервис нам нужен
            val componentName= ComponentName(this,MyJobService::class.java)

            //содержит все требования для нашего сервиса(ограничения)
            val jobInfo=JobInfo.Builder(MyJobService.JOB_ID,componentName)
            //2.1указываем ограничения для нашего сервиса
            //2.2 хотим чтобы сервис работал на устройстве которое сейчас заряжается
                    //как только отключим от зарядки сервис остановит свое выполнение
                .setRequiresCharging(true)
                //2.3 хотим чтобы работал если на устройстве включен WI_FI
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                //если хотим чтобы сервис запускался после выключения и включения телефона
                .setPersisted(true)
                //если хотим установить какой то интервал запуска сервиса
                //.setPeriodic()
                .build()

            //3 запуск на выполнение
            val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
            jobScheduler.schedule(jobInfo)
        }
    }
}