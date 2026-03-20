package com.nutritrack.app.data.db

import androidx.room.*
import com.nutritrack.app.data.model.SupplementLog
import kotlinx.coroutines.flow.Flow

@Dao
interface SupplementLogDao {
    @Query("SELECT * FROM supplement_log WHERE dateEpochDay = :epochDay ORDER BY name ASC")
    fun getSupplementsForDay(epochDay: Long): Flow<List<SupplementLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(log: SupplementLog): Long

    @Query("UPDATE supplement_log SET isTaken = :taken WHERE id = :id")
    suspend fun setTaken(id: Int, taken: Boolean)

    @Delete
    suspend fun delete(log: SupplementLog)

    @Query("""
        INSERT OR IGNORE INTO supplement_log (name, doseDescription, isTaken, dateEpochDay, timestampMs)
        SELECT name, doseDescription, 0, :targetDay, :now
        FROM supplement_log
        WHERE dateEpochDay = :sourceDay
    """)
    suspend fun copySupplementsToDay(sourceDay: Long, targetDay: Long, now: Long)
}
