package com.nutritrack.app.data.db

import androidx.room.*
import com.nutritrack.app.data.model.FoodCategory
import com.nutritrack.app.data.model.FoodItem
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodItemDao {
    @Query("SELECT * FROM food_item ORDER BY name ASC")
    fun getAllFoods(): Flow<List<FoodItem>>

    @Query("SELECT * FROM food_item WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchFoods(query: String): Flow<List<FoodItem>>

    @Query("SELECT * FROM food_item WHERE category = :category ORDER BY name ASC")
    fun getFoodsByCategory(category: FoodCategory): Flow<List<FoodItem>>

    @Query("SELECT * FROM food_item WHERE id = :id")
    suspend fun getFoodById(id: Int): FoodItem?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(foods: List<FoodItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(food: FoodItem): Long

    @Delete
    suspend fun delete(food: FoodItem)

    @Query("SELECT COUNT(*) FROM food_item")
    suspend fun count(): Int
}
