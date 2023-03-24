package com.example.kotlin

import android.app.Activity
import android.app.Application
import android.content.Context
import android.provider.Telephony.Threads
import androidx.room.Room
import com.example.kotlin.domian.room.HistoryDao
import com.example.kotlin.domian.room.MyDataBase
import com.example.kotlin.lesson6.ThreadsFragment
import kotlin.concurrent.thread

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

    companion object {
        private var dataBase: MyDataBase? = null
        private var appContext: MyApp? = null


        fun getHistoryDao(): HistoryDao {
            if (dataBase == null) {
                if (appContext != null) {

                       dataBase =
                            Room.databaseBuilder(appContext!!, MyDataBase::class.java, "test")
                                .allowMainThreadQueries()
                                .build()

                } else {
                    throw java.lang.IllegalStateException("что-то пошло не так, пуст appContext")
                }
            }
            return dataBase!!.historyDao()
        }
    }
}