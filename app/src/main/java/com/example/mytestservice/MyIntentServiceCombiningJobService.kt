package com.example.mytestservice

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat

class MyIntentServiceCombiningJobService : IntentService(NAME) {
    override fun onCreate() {
        super.onCreate()
        log("onCreate")
        //используется вместо return START_STICKY и тд
        setIntentRedelivery(true) //= START_REDELIVERY_INTENT -чтобы интент успешно доставлялся при перзапуске сервиса
        //setIntentRedelivery(false) // = START_NOT_STICKY

    }
    override fun onHandleIntent(intent: Intent?) {
        log("onHandleIntent")
        //2)Получаем номер стр
        val page=intent?.getIntExtra(PAGE,0) ?: 0
        for (i in 0 until 5) {
            Thread.sleep(1000)
            log("Timer: $i page load: $page")
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
    }

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "MyIntentServiceCombiningJobService : $message")
    }

    companion object {
        private const val NAME="MyIntentService"
        private const val PAGE="page"
        //1) передаем номер стр
        fun newIntent(context: Context,page : Int): Intent {
            return Intent(context, MyIntentServiceCombiningJobService::class.java).apply {
                putExtra(PAGE,page)
            }
        }
    }
}