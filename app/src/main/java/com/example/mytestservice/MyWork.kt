package com.example.mytestservice

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.*

class MyWork(context: Context, private val workerParameters: WorkerParameters) : Worker(
    context,
    workerParameters
) {

    //вся работа выполняется тут
    //работает в фоновом потоке
    override fun doWork(): Result {
        log("doWork")
        //1)достаем стр
        //Класс Data очень похож на Bundle - там хранятся все объекты парами (ключ/значение)
        val page = workerParameters.inputData.getInt(PAGE, 0)
        for (i in 0 until 5) {
            Thread.sleep(1000)
            log("Timer: $i page load: $page")
        }
        //doWork возвращает объект Result он принимает одно их 3 значений:
        //1- return Result.success() - все прошло успешно и сервис завершил свою работу
        //2- return Result.failure() - что то пошло не так метод завершился с исключением, а тут перезапущен
        //не будет
        //3- return Result.retry() - что то пошло не так метод завершился с исключением, но он будет
        //перезапущен
        return Result.success()

    }

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "MyWorkManager: $message")
    }

    companion object {
        private const val PAGE = "page"
        const val WORKER_NAME = "work name"

        //3 создаем экземпляр реквеста
        fun makeRequest(page: Int): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<MyWork>()
                //3.1) положим номер стр
                //workDataOf() - принмиает в качестве параметра объект Pair - это объект который хранит
                //один ключ / одно значение в итоге получим объект Data
                .setInputData(workDataOf(PAGE to page))
                //3.2)кладем ограничения
                .setConstraints(makeConstrains())
                .build()
        }

        private fun makeConstrains(): Constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .build()

    }
}