package com.nutritrack.app.data.db

import androidx.room.*
import com.nutritrack.app.data.model.FoodLogEntry
import com.nutritrack.app.data.model.MealType
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodLogDao {
    @Query("SELECT * FROM food_log_entry WHERE dateEpochDay = :epochDay ORDER BY timestampMs ASC")
    fun getEntriesForDay(epochDay: Long): Flow<List<FoodLogEntry>>

    @Query("SELECT * FROM food_log_entry WHERE dateEpochDay = :epochDay AND mealType = :meal ORDER BY timestampMs ASC")
    fun getEntriesForMeal(epochDay: Long, meal: MealType): Flow<List<FoodLogEntry>>

    @Query("""
        SELECT COALESCE(SUM(calories), 0) FROM food_log_entry WHERE dateEpochDay = :epochDay
    """)
    fun getTotalCaloriesForDay(epochDay: Long): Flow<Float>

    @Query("""
        SELECT COALESCE(SUM(proteinG), 0) FROM food_log_entry WHERE dateEpochDay = :epochDay
    """)
    fun getTotalProteinForDay(epochDay: Long): Flow<Float>

    @Insert
    suspend fun insert(entry: FoodLogEntry)

    @Delete
    suspend fun delete(entry: FoodLogEntry)

    @Query("DELETE FROM food_log_entry WHERE id = :id")
    suspend fun deleteById(id: Int)
}
