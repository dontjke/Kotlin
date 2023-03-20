package com.example.kotlin.lesson6

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.example.kotlin.utils.KEY_SERVICE

class MainService(val name: String = "") : IntentService(name) {
    override fun onHandleIntent(p0: Intent?) {
        Log.d("@@@", "work MainService")
        p0?.let {
            val extra = it.getStringExtra(KEY_SERVICE)
            Log.d("@@@", "work MainService $extra")
        }
    }
}