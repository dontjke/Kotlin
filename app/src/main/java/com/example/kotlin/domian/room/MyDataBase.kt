package com.example.kotlin.domian.room

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities = arrayOf(HistoryEntity::class), version = 2)
abstract class MyDataBase:RoomDatabase() { //база данных . работаем с таблицей как с объектом
    abstract fun historyDao():HistoryDao
    //abstract fun historyDao1():HistoryDao
   // abstract fun historyDao2():HistoryDao
}