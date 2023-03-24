package com.example.kotlin.domian.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_table")
data class HistoryEntity(  //таблица
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val city: String,
    val temperature: Int,
   /* val timestamp: Long,*/
    val feelsLike: Int,
    val icon: String

)
