package com.nutritrack.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class FoodCategory {
    MEAT,
    FISH,
    DAIRY,
    EGGS,
    VEGETABLE,
    FRUIT,
    CEREAL,
    LEGUME,
    NUT,
    SUPPLEMENT,
    OTHER
}

@Entity(tableName = "food_item")
data class FoodItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val category: FoodCategory,
    // All values are per 100g
    val caloriesPer100g: Float,
    val proteinPer100g: Float,
    val carbsPer100g: Float,
    val fatPer100g: Float,
    val fiberPer100g: Float = 0f,
    // Unit displayed to user (g, ml, unit)
    val servingUnit: String = "g",
    val isCustom: Boolean = false  // true = added by user
)