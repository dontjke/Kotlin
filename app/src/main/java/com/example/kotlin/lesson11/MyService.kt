package com.example.kotlin.lesson11

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.kotlin.R
import com.example.kotlin.view.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyService : FirebaseMessagingService() {


    override fun onMessageReceived(message: RemoteMessage) { //принимаю сообщение
        Log.d("@@@", "$message")
        if (message.data.isNullOrEmpty()) {
            val title = message.data[KEY_TITLE]
            val message = message.data[KEY_MESSAGE]
            if (!title.isNullOrEmpty() && !message.isNullOrEmpty()) {
                push(title, message) //пробрасываю далее
            }
        }
    }

    companion object {
        private const val NOTIFICATION_LOW = 1
        private const val NOTIFICATION_HIGH = 2
        private const val CHANNEL_LOW = "channel_low"
        private const val CHANNEL_HIGH = "channel_high"

        private const val KEY_TITLE = "myTitle"
        private const val KEY_MESSAGE = "myMessage"
    }

    private fun push(title: String, message: String) { //уведомления
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        //val intent = Intent(applicationContext, MainActivity::class.java)
       // val contentIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)

        /*val notificationBuilderLow = NotificationCompat.Builder(this, CHANNEL_LOW).apply {
            setSmallIcon(R.drawable.ic_map_pin)
            setContentTitle(getString(R.string.notification_title_low))
            setContentText(getString(R.string.notification_text_low))
            priority = NotificationManager.IMPORTANCE_LOW
        }*/
        val notificationBuilderHigh = NotificationCompat.Builder(this, CHANNEL_HIGH).apply {
            setSmallIcon(R.drawable.ic_map_marker)
            setContentTitle(title)
            setContentText(message)
           // setContentIntent(contentIntent)
            priority = NotificationManager.IMPORTANCE_HIGH
        }


        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){ //каналы
            val channelNameLow = "Name $CHANNEL_LOW"
            val channelDescriptionLow = "Description $CHANNEL_LOW"
            val channelPriorityLow = NotificationManager.IMPORTANCE_LOW
            val channelLow = NotificationChannel(CHANNEL_LOW,channelNameLow,channelPriorityLow).apply {
                description = channelDescriptionLow
            }
            notificationManager.createNotificationChannel(channelLow)
        }
        notificationManager.notify(NOTIFICATION_LOW,notificationBuilderLow.build())*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelNameHigh = "Name $CHANNEL_HIGH"
            val channelDescriptionHigh = "Description $CHANNEL_HIGH"
            val channelPriorityHigh = NotificationManager.IMPORTANCE_HIGH
            val channelHigh =
                NotificationChannel(CHANNEL_HIGH, channelNameHigh, channelPriorityHigh).apply {
                    description = channelDescriptionHigh
                }
            notificationManager.createNotificationChannel(channelHigh)
        }
        notificationManager.notify(NOTIFICATION_HIGH, notificationBuilderHigh.build())
    }


    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}
