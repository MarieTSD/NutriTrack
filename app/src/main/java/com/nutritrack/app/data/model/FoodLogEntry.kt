package com.nutritrack.app.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

enum class MealType {
    BREAKFAST,
    LUNCH,
    DINNER,
    SNACK
}

@Entity(
    tableName = "food_log_entry",
    foreignKeys = [
        ForeignKey(
            entity = FoodItem::class,
            parentColumns = ["id"],
            childColumns = ["foodItemId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("foodItemId")]
)
data class FoodLogEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val foodItemId: Int,
    val foodName: String,       // Stored snapshot so name survives food edits
    val mealType: MealType,
    val quantityG: Float,       // Amount eaten in grams (or ml)
    val calories: Float,        // Pre-calculated at log time
    val proteinG: Float,
    val carbsG: Float,
    val fatG: Float,
    val dateEpochDay: Long,     // LocalDate.toEpochDay() — easy day-level queries
    val timestampMs: Long = System.currentTimeMillis()
)
