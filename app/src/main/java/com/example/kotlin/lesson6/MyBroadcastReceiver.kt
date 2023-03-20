package com.example.kotlin.lesson6

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.kotlin.utils.KEY_BUNDLE_SERVICE_MESSAGE

import java.lang.Thread.sleep

class MyBroadcastReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            val extra = it.getStringExtra(KEY_BUNDLE_SERVICE_MESSAGE)
            Log.d("@@@", "MyBroadcastReceiver onReceive $extra")
        }
    }
}