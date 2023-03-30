package com.example.kotlin

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.kotlin.domian.room.HistoryDao
import com.example.kotlin.domian.room.MyDataBase

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
                            //.allowMainThreadQueries()
                            .addMigrations(migration_1_2)
                            .build()

                } else {
                    throw java.lang.IllegalStateException("что-то пошло не так, пуст appContext")
                }
            }
            return dataBase!!.historyDao()
        }

        private val migration_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE history_table ADD column condition TEXT NOT NULL DEFAULT ''")
            }

        }
    }
}