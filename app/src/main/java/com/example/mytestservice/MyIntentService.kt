package com.example.mytestservice

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*

class MyIntentService : IntentService(NAME) {
    override fun onCreate() {
        super.onCreate()
        log("onCreate")
        //используется вместо return START_STICKY и тд
        //setIntentRedelivery(true) = START_REDELIVERY_INTENT
        setIntentRedelivery(false) // = START_NOT_STICKY
        createNotificationChannel()
        createNotification()
        startForeground(NOTIFICATION_ID, createNotification())
    }

    //1) переопределяем вместо onStartCommand()
    //код внутри этого метода будет выполнятся в другом потоке
    override fun onHandleIntent(intent: Intent?) {
        log("onHandleIntent")
        for (i in 0 until 3) {
            Thread.sleep(1000)
            log("Timer: $i")
        }
        //сразу после выполнения данного метода Сервис будет остановлен автоматический
    }
    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
    }


    private fun log(message: String) {
        Log.d("SERVICE_TAG", "MyForegroundService : $message")
    }
    //onBind переопределять не надо

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("AppJuke")
            .setContentText("Your get message")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
    }

    companion object {
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "channel_name"
        private const val NOTIFICATION_ID = 1
        private const val NAME="MyIntentService"

        fun newIntent(context: Context): Intent {
            return Intent(context, MyIntentService::class.java)
        }
    }
}