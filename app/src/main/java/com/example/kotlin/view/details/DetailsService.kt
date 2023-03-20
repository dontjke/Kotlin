package com.example.kotlin.view.details

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.example.kotlin.utils.*

class DetailsService(val name: String = "") : IntentService(name) {
    override fun onHandleIntent(p0: Intent?) {
        Log.d("@@@", "work DetailsService")
        p0?.let {
            val lat = it.getStringExtra(KEY_BUNDLE_LAT) //получаю от  в сервис
            val lon = it.getStringExtra(KEY_BUNDLE_LON) //получаю от  в сервис
            Log.d("@@@", "work DetailsService$lat $lon")
            val message = Intent(KEY_VIBE)  //создаю ответ от сервиса в активити
            message.putExtra(KEY_BUNDLE_SERVICE_MESSAGE,"привет активити")  //сообщение в активити
            sendBroadcast(message) //отправляю
            //LocalBroadcastManager.getInstance(this).sendBroadcast(message) //отправляю локально
        }
    }
}