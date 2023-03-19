package com.example.mytestservice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import com.example.mytestservice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var id = 0

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.simpleService.setOnClickListener {
            startService(MyService.newIntent(this, 10))
        }
        binding.foregroundService.setOnClickListener {
            showNotification()
        }
    }

    private fun showNotification() {
        //1.4 за отображение уведомелнии отвечает класс
        //2т.к канал создается на версии начиная с 8 делаем проверку
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
           val notificationChannel= NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                //в зависимости от приоритета уведомелния мб показано по верх всех оконо,со звуковым
                //уведомелнием и тд
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

        //1создаем уведомелние
        // Builder устаревший метод
        //NotificationCompat проверка деалется автоматиеский
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            //1.1 устанавливаем заголовк для пользовтеля
            .setContentTitle("Title")
            //1.2 устанавливаем текст сообщения
            .setContentTitle("TEXT")
            //1.3 устанавливаем иконку
            .setSmallIcon(R.drawable.ic_launcher_background)
            .build()

        //отвечает за отображение уведомлений
        //val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        //id устанавливаем 1 чтобы если при нажатии 10 раз, отображалось всего 1 уведомление
        //если будет каждый раз устанавливать новый, то каждый раз будет отображаться new уведомление
        //Все работает если запусскать на версии ниже 8
        //Запустим на версии выше (Api 26) = 8
        //То уведомелний никаких не будет,т.к с 8 версии любое уведомление должно быть в канале,для
        //каждого уведомления должен быть свой канал
        notificationManager.notify(id++, notification)
    }

    companion object {
        private const val CHANNEL_ID = "channel_id"

        //нужен если пользовтель захочет залокирвоать наши уведомления, то он может это сделать
        //долгим нажатием на уведомелние и там отображается имя канала в котормо отображается
        //уведомление и user может отключать различные каналы(поэтому надо давать нормальные имена
        //каналам, чтобы пользовтель понимал, что он отключает
        private const val CHANNEL_NAME = "channel_name"

    }
}