package com.example.kotlin.lesson6

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.example.kotlin.utils.KEY_BUNDLE_ACTIVITY_MESSAGE
import com.example.kotlin.utils.KEY_BUNDLE_SERVICE_MESSAGE

import com.example.kotlin.utils.KEY_VIBE


class MainService(val name: String = "") : IntentService(name) {
    override fun onHandleIntent(p0: Intent?) {
        Log.d("@@@", "work MainService")
        p0?.let {
            val extra = it.getStringExtra(KEY_BUNDLE_ACTIVITY_MESSAGE) //получаю от активити в сервис
            Log.d("@@@", "work MainService $extra")
            val message = Intent(KEY_VIBE)  //создаю ответ от сервиса в активити
            message.putExtra(KEY_BUNDLE_SERVICE_MESSAGE,"привет активити")  //сообщение в активити
            sendBroadcast(message) //отправляю
            //LocalBroadcastManager.getInstance(this).sendBroadcast(message) //отправляю локально
        }
    }
}