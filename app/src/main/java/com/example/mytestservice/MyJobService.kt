package com.example.mytestservice

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.Build
import android.os.PersistableBundle
import android.util.Log
import kotlinx.coroutines.*

class MyJobService : JobService() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        log("onStartJob")
        //3 достаем сервисе из очереди
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            coroutineScope.launch {
                // получаем из params вызывая dequeueWork() таким образом из очереди будет взять 1 сервис
                var workItem = params?.dequeueWork()
                //допустим мы запустили сервис 10 раз и все 10 сервисов будут положены в очередь
                //тогда в цикле нам надо 10 раз вызывать метод dequeueWork() и получаем знач для стр
                //деалем до тех пор пока dequeueWork() не вернет null, пока внутри параметров есть сервисы
                //будем из workItem получать стр и запускать на выполнение
                while (workItem != null) {
                    //работаем уже с конкретным сервисом
                    val page = workItem.intent.getIntExtra(PAGE, 0)
                    for (i in 0 until 5) {
                        delay(1000)
                        log("Timer: $i Number page:$page")
                    }
                    //4 после того как цикл выполнен нам надо заврешить не всю работу,а завершить только
                    //текущий сервис
                    //означает что тот сервис который лежал в очерди завершил свою работу
                    params?.completeWork(workItem)
                    //после того как завершили работу мы снова достаем новый объект из очереди
                    workItem = params?.dequeueWork()
                }
                //означает что весь сервис полностью завершил свою работу и никаких сервисов в очереди
                //не осталось
                jobFinished(params, false)
            }
        }
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        log("onStopJob")
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        log("onDestroy")
    }

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "MyJobService : $message")
    }

    companion object {
        const val JOB_ID = 1
        private const val PAGE = "page"

        //2
        fun newIntent(page: Int): Intent {
            return Intent().apply {
                putExtra(PAGE, page)
            }
        }
    }

}