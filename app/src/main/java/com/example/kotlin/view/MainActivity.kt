package com.example.kotlin.view

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.kotlin.MyApp
import com.example.kotlin.R
import com.example.kotlin.lesson10.MapsFragment
import com.example.kotlin.lesson6.MainService
import com.example.kotlin.lesson6.MyBroadcastReceiver
import com.example.kotlin.lesson6.ThreadsFragment
import com.example.kotlin.lesson9.WorkWithContentProviderFragment
import com.example.kotlin.utils.KEY_BUNDLE_ACTIVITY_MESSAGE
import com.example.kotlin.utils.KEY_SP_FILE_NAME_1
import com.example.kotlin.utils.KEY_SP_FILE_NAME_1_KEY_IS_RUSSIAN
import com.example.kotlin.utils.KEY_VIBE
import com.example.kotlin.view.weatherlist.HistoryWeatherListFragment
import com.example.kotlin.view.weatherlist.WeatherListFragment
import okhttp3.internal.notify

class MainActivity : AppCompatActivity() {

    companion object {
        private const val NOTIFICATION_LOW = 1
        private const val NOTIFICATION_HIGH = 2
        private const val CHANNEL_LOW = "channel_low"
        private const val CHANNEL_HIGH = "channel_high"

    }

    private fun push() { //уведомления
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilderLow = NotificationCompat.Builder(this, CHANNEL_LOW).apply {
            setSmallIcon(R.drawable.ic_map_pin)
            setContentTitle(getString(R.string.notification_title_low))
            setContentText(getString(R.string.notification_text_low))
            priority = NotificationManager.IMPORTANCE_LOW
        }
        val notificationBuilderHigh = NotificationCompat.Builder(this, CHANNEL_HIGH).apply {
            setSmallIcon(R.drawable.ic_map_marker)
            setContentTitle(getString(R.string.notification_title_high))
            setContentText(getString(R.string.notification_text_high))
            priority = NotificationManager.IMPORTANCE_HIGH
        }




        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){ //каналы
            val channelNameLow = "Name $CHANNEL_LOW"
            val channelDescriptionLow = "Description $CHANNEL_LOW"
            val channelPriorityLow = NotificationManager.IMPORTANCE_LOW
            val channelLow = NotificationChannel(CHANNEL_LOW,channelNameLow,channelPriorityLow).apply {
                description = channelDescriptionLow
            }
            notificationManager.createNotificationChannel(channelLow)
        }
        notificationManager.notify(NOTIFICATION_LOW,notificationBuilderLow.build())

        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
            val channelNameHigh = "Name $CHANNEL_HIGH"
            val channelDescriptionHigh = "Description $CHANNEL_HIGH"
            val channelPriorityHigh = NotificationManager.IMPORTANCE_HIGH
            val channelHigh = NotificationChannel(CHANNEL_HIGH,channelNameHigh,channelPriorityHigh).apply {
                description = channelDescriptionHigh
            }
            notificationManager.createNotificationChannel(channelHigh)
        }
        notificationManager.notify(NOTIFICATION_HIGH,notificationBuilderHigh.build())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, WeatherListFragment.newInstance())
                .commit()
        }

        startService(Intent(this, MainService::class.java).apply {//создали сервис
            putExtra(KEY_BUNDLE_ACTIVITY_MESSAGE, "привет сервис")
        })


        val receiver = MyBroadcastReceiver() //создали приемник
        registerReceiver(receiver, IntentFilter(KEY_VIBE)) //волна
        // registerReceiver(receiver, IntentFilter("android.intent.action.AIRPLANE_MODE"))   //для дз
        //LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter("myaction"))

        val sp = getSharedPreferences(KEY_SP_FILE_NAME_1, Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putBoolean(
            KEY_SP_FILE_NAME_1_KEY_IS_RUSSIAN,
            true
        ) // сохраняем в SP состояние кнопки локации Россия/мир
        editor.apply()

        val defaultValueIsRussian = true
        sp.getBoolean(KEY_SP_FILE_NAME_1_KEY_IS_RUSSIAN, defaultValueIsRussian)

        Thread {
            MyApp.getHistoryDao().getAll()
        }.start()

        push()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_threads -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, ThreadsFragment.newInstance())
                    .addToBackStack("")
                    .commit()
            }
            R.id.action_history -> {
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.container, HistoryWeatherListFragment.newInstance())
                    .addToBackStack("")
                    .commit()
            }
            R.id.action_work_with_content_provider -> {
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.container, WorkWithContentProviderFragment.newInstance())
                    .addToBackStack("")
                    .commit()
            }
            R.id.action_google_maps -> {
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.container, MapsFragment())
                    .addToBackStack("")
                    .commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}