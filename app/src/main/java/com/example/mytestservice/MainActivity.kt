package com.example.mytestservice

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.app.job.JobWorkItem
import android.content.ComponentName
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.example.mytestservice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var page = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.simpleService.setOnClickListener {
            stopService(MyForegroundService.newIntent(this))
            startService(MyService.newIntent(this, 10))
        }
        binding.foregroundService.setOnClickListener {
            ContextCompat.startForegroundService(
                this,
                MyForegroundService.newIntent(this)
            )
        }
        binding.intentService.setOnClickListener {
            ContextCompat.startForegroundService(
                this,
                MyIntentService.newIntent(this)
            )
        }
        binding.jobScheduler.setOnClickListener {
            val componentName = ComponentName(this, MyJobService::class.java)

            val jobInfo = JobInfo.Builder(MyJobService.JOB_ID, componentName)
                .setRequiresCharging(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .build()

            val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val intent = MyJobService.newIntent(page++)
                jobScheduler.enqueue(jobInfo, JobWorkItem(intent))
            } else {
                startService(MyIntentServiceCombiningJobService.newIntent(this, page++))
            }
        }
        binding.jobIntentService.setOnClickListener {
            MyJobIntentService.enqueue(this, page++)
        }
        //2
        binding.workManager.setOnClickListener {
            //2.1) получаем экземпляр воркМенеджера
            // и передаем контекс всего приложения чтобы не было утечекПамяти(когда активити не сущ,
            //а воркменеджер все еще работает
            val workManager = WorkManager.getInstance(applicationContext)
            //2.2)
            //enqueueUniqueWork() - принимает имяВорекра и если мы запустим несколько экземпляров
            //одного и тогоже воркера и в случае с enqueue() все эти воркеры начнут выполнение,а
            //в случае enqueueUniqueWork() можем самостоятельно указать что делать  если была попытка
            //запустить воркер который уже запущен
            workManager.enqueueUniqueWork(
                MyWorker.WORKER_NAME,
                //передаем что делать если работа уже запущена их 4 варианта:
                //отличии APPEND и APPEND_OR_REPLACE отличие в том что
                //как будут реагировать воркеры лежащие в очереди на то что какой то ворекр был завершен
                //с ошибкой
                //1- ExistingWorkPolicy.APPEND - новый worker будет добавлен в очередь,в данном случае
                //если какой то из сервисов был завершен с ошибкой, то эта ошибка распространится и на все
                //дальнейшие сервисы в очереди
                //2- ExistingWorkPolicy.APPEND_OR_REPLACE - новый worker будет добавлен в очередь,
                //в случае ошибки будет создана нова цепочка
                //3- ExistingWorkPolicy.KEEP - существующий воркер продолжит выполнение, а новый будет проигнорирован
                //4- ExistingWorkPolicy.REPLACE - в данном случае существующий воркер будет заменен новым

                ExistingWorkPolicy.APPEND,
                //2.3) принимает OneTimeWorkRequest - не надо повторят загрузку заново, после того как
                //она завершилась
                //Также данный реквест принимаем все параметры (PAGE), а также огранчиение на работу Ворекра
                //вроде того как в JobService()
                MyWorker.makeRequest(page++)

            )
        }
    }
}