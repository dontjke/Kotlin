package com.example.kotlin.lesson6

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.kotlin.utils.KEY_BROADCAST

import java.lang.Thread.sleep

class MyBroadcastReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            val extra = it.getStringExtra(KEY_BROADCAST)
            Log.d("@@@", "MyBroadcastReceiver onReceive $extra")
            sleep(1000L)

        }

    }
}