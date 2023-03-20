package com.example.mytestservice

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import kotlinx.coroutines.*

class MyJobService : JobService() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }
    //вызывается при старте сервиса и тут выполняется вся работа
    //тут код выполняется на главном потоке
    //код внутри метода мб синхроным (обычный метод который будет выполнятся последовательно)
    //возвращаемый тип обозначает ваша работа все еще выполняется или нет
    //в нашем случае мы запускаем корутину и выходим из метода onStartJob, но работа еще не завершена
    // т.к мы выполняем асинхронные операции
    //тогда возвращем true
    //так скажем что сервис еще выполняется и мы сами заврешим работу когда это необходмо при помощи
    // метода  jobFinished()
    //если бы делали синхроную работу onStartJob обозначало что закночило выполнение
    // тогда return false
    override fun onStartJob(params: JobParameters?): Boolean {
        log("onStartCommand")
        coroutineScope.launch {
            for (i in 0 until 3) {
                delay(1000)
                log("Timer: $i")
            }
            //принимает 1 - JobParameters,
            //2- обозначает надо ли запланировать выполнение сервиса заного
            //Например: обнвовление данных в фоне, оно должно происходить постоянно с каким то таймаутом
            //тогда return true - тогда сервис будет перезапущен через какое то время
            // return false - перезапущен не будет
            jobFinished(params,true)
        }
        return true
    }
    //т.к на jobService можно устанавливать ограниченя например(сервис выполняется если устройство заряжается)
    //и если мы отключаем его от зарядки, то сервис будет останволен и вызовется метод onStopJob()
    //НО если мы сами остановили сервис с jobFinished(), то метод onStopJob() вызыван не будет
    //ОН вызывается тогда, когда система убила наш сервис
    override fun onStopJob(params: JobParameters?): Boolean {
       log("onStopJob")
        //если хотим после убийства сервиса он был заного выполнен
        return true
        // иначе  return false
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        log("onDestroy")
    }
    private fun log(message: String) {
        Log.d("SERVICE_TAG", "MyService : $message")
    }

}