package com.example.mytestservice

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*

class MyForegroundService : Service() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)


    private val notificationManager by lazy {
        getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }


    private val notificationBuilder by lazy {
        createNotificationBuilder()
    }

    //1)Слушатель на изменение прогресса
    var onProgressChanged: ((Int)->Unit)? = null

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand")
        coroutineScope.launch {
            for (i in 0..100 step 5) {
                delay(1000)
                val notification = notificationBuilder
                    .setProgress(100, i, false)
                    .build()
                notificationManager.notify(NOTIFICATION_ID,notification )
                //2)Оповещаем слушатель об уведомлении
                onProgressChanged?.invoke(i)
                log("Timer: $i")
            }

            stopSelf()
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        log("onDestroy")
    }

    //4)
    override fun onBind(intent: Intent?): IBinder {
        return LocalBinder()
    }

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "MyForegroundService : $message")
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    //1)
    private fun createNotificationBuilder() = NotificationCompat.Builder(this, CHANNEL_ID)
        .setContentTitle("AppJuke")
        .setContentText("Your get message")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setProgress(100, 0, false)
        .setOnlyAlertOnce(true)

    //5)Создадим свой Binder
    inner class LocalBinder : Binder(){
        fun getService() = this@MyForegroundService
    }

    companion object {
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "channel_name"
        private const val NOTIFICATION_ID = 1
        fun newIntent(context: Context): Intent {
            return Intent(context, MyForegroundService::class.java)
        }
    }
}