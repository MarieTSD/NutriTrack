package com.nutritrack.app.data.db

import androidx.room.*
import com.nutritrack.app.data.model.WaterLog
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterLogDao {
    @Query("SELECT * FROM water_log WHERE dateEpochDay = :epochDay ORDER BY timestampMs ASC")
    fun getEntriesForDay(epochDay: Long): Flow<List<WaterLog>>

    @Query("SELECT COALESCE(SUM(amountMl), 0) FROM water_log WHERE dateEpochDay = :epochDay")
    fun getTotalForDay(epochDay: Long): Flow<Int>

    @Insert
    suspend fun insert(log: WaterLog)

    @Delete
    suspend fun delete(log: WaterLog)
}
