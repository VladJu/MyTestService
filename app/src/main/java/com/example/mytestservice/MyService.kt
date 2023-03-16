package com.example.mytestservice

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*

class MyService : Service() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    //вызывается при создании
    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }

    //вся работа которая будет выполнятся при запуске сервиса
    //вся работа выполняется на main потоке
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand")
        coroutineScope.launch {
            for (i in 0 until 100){
                delay(1000)
                log("Timer: $i")
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    //вызывается при уничтожении сервиса
    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        log("onDestroy")
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    private fun log(message: String){
        Log.d("SERVICE_TAG", "MyService : $message")
    }

    companion object{

        fun newIntent(context: Context) : Intent{
            return Intent(context,MyService::class.java)

        }
    }

}