package com.example.kotlin.domian.room

import androidx.room.Entity
import androidx.room.PrimaryKey

const val ID = "id"
const val NAME = "city"
const val TEMPERATURE = "temperature"
const val FEELS_LIKE = "feelsLike"
const val ICON = "icon"
const val CONDITION = "condition"
@Entity(tableName = "history_table")
data class HistoryEntity(  //таблица
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val city: String,
    val temperature: Int,
   /* val timestamp: Long,*/
    val feelsLike: Int = 10,
    val icon: String ="skn_n",
    val condition: String = "cloudy"
)
