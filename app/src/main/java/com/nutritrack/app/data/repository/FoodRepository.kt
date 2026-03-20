package com.nutritrack.app.data.repository

import com.nutritrack.app.data.db.FoodItemDao
import com.nutritrack.app.data.db.FoodLogDao
import com.nutritrack.app.data.model.FoodItem
import com.nutritrack.app.data.model.FoodLogEntry
import com.nutritrack.app.data.model.MealType
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodRepository @Inject constructor(
    private val foodItemDao: FoodItemDao,
    private val foodLogDao: FoodLogDao
) {
    // ── Food database ────────────────────────────────────────────
    fun searchFoods(query: String): Flow<List<FoodItem>> =
        foodItemDao.searchFoods(query)

    fun getAllFoods(): Flow<List<FoodItem>> =
        foodItemDao.getAllFoods()

    suspend fun getFoodById(id: Int): FoodItem? =
        foodItemDao.getFoodById(id)

    suspend fun addCustomFood(food: FoodItem): Long =
        foodItemDao.upsert(food)

    suspend fun seedFoodDatabase(foods: List<FoodItem>) =
        foodItemDao.insertAll(foods)

    suspend fun foodDatabaseCount(): Int =
        foodItemDao.count()

    // ── Food log ─────────────────────────────────────────────────
    fun getEntriesForDay(date: LocalDate): Flow<List<FoodLogEntry>> =
        foodLogDao.getEntriesForDay(date.toEpochDay())

    fun getEntriesForMeal(date: LocalDate, meal: MealType): Flow<List<FoodLogEntry>> =
        foodLogDao.getEntriesForMeal(date.toEpochDay(), meal)

    fun getTotalCaloriesForDay(date: LocalDate): Flow<Float> =
        foodLogDao.getTotalCaloriesForDay(date.toEpochDay())

    fun getTotalProteinForDay(date: LocalDate): Flow<Float> =
        foodLogDao.getTotalProteinForDay(date.toEpochDay())

    /**
     * Log a food entry — calculates macros from quantity at log time
     */
    suspend fun logFood(
        foodItem: FoodItem,
        quantityG: Float,
        meal: MealType,
        date: LocalDate
    ) {
        val ratio = quantityG / 100f
        val entry = FoodLogEntry(
            foodItemId  = foodItem.id,
            foodName    = foodItem.name,
            mealType    = meal,
            quantityG   = quantityG,
            calories    = foodItem.caloriesPer100g * ratio,
            proteinG    = foodItem.proteinPer100g  * ratio,
            carbsG      = foodItem.carbsPer100g    * ratio,
            fatG        = foodItem.fatPer100g      * ratio,
            dateEpochDay = date.toEpochDay()
        )
        foodLogDao.insert(entry)
    }

    suspend fun deleteLogEntry(entry: FoodLogEntry) =
        foodLogDao.delete(entry)
}
