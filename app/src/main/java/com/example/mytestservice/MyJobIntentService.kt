package com.example.mytestservice

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService

class MyJobIntentService : JobIntentService() {
    override fun onCreate() {
        super.onCreate()
        log("onCreate")

        //setIntentRedelivery() - даннго метода нет,но он по умолчанию true
    }

    override fun onHandleWork(intent: Intent) {
        log("onHandleWork")
        //1)Получаем номер стр
        val page=intent.getIntExtra(PAGE,0)
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
        Log.d("SERVICE_TAG", "MyJobIntentService : $message")
    }

    companion object {
        private const val PAGE="page"
        private const val JOB_ID=1

        //3) Для запуска данного сервиса необходимо вызывать статичекий метод enqueueWork()
        fun enqueue(context: Context,page: Int){
           enqueueWork(
               context,
               MyJobIntentService::class.java,
               JOB_ID,
               newIntent(context,page)
           )
        }
        //2) передаем номер стр
        private fun newIntent(context: Context,page : Int): Intent {
            return Intent(context, MyJobIntentService::class.java).apply {
                putExtra(PAGE,page)
            }
        }
    }
}