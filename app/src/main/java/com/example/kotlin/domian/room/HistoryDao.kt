package com.example.kotlin.domian.room

import android.database.Cursor
import androidx.room.*

@Dao
interface HistoryDao {
    @Query("INSERT INTO history_table (city,temperature, feelsLike, icon) VALUES(:city,:temperature,:feelsLike,:icon)")
    fun nativeInsert(city: String, temperature: Int, feelsLike: Int, icon: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: HistoryEntity)

    @Delete
    fun delete(entity: HistoryEntity)
    @Query("DELETE FROM history_table WHERE id =:id")
    fun deleteById(id:Long)

    @Update
    fun update(entity: HistoryEntity)

    @Query("SELECT * FROM history_table")
    fun getAll(): List<HistoryEntity>

    @Query("SELECT * FROM history_table WHERE city=:city")
    fun getHistoryForCity(city: String): List<HistoryEntity>

    @Query("SELECT * FROM history_table WHERE id =:id")
    fun getHistoryCursor(id: Long): Cursor

    @Query("SELECT * FROM history_table")
    fun getHistoryCursor(): Cursor
}