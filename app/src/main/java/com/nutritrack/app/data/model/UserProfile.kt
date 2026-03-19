package com.nutritrack.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ActivityLevel {
    SEDENTARY,          // Little or no exercise
    LIGHTLY_ACTIVE,     // Light exercise 1–3 days/week
    MODERATELY_ACTIVE,  // Moderate exercise 3–5 days/week
    VERY_ACTIVE,        // Hard exercise 6–7 days/week
    EXTRA_ACTIVE        // Very hard exercise + physical job
}

enum class Goal {
    LOSE_WEIGHT,
    MAINTAIN_WEIGHT,
    GAIN_MUSCLE
}

enum class Sex {
    MALE,
    FEMALE
}

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1, // Single profile — always ID 1
    val name: String = "",
    val age: Int = 25,
    val sex: Sex = Sex.MALE,
    val weightKg: Float = 70f,
    val heightCm: Float = 170f,
    val muscleMassPercent: Float = 40f,
    val bodyFatPercent: Float = 20f,
    val activityLevel: ActivityLevel = ActivityLevel.MODERATELY_ACTIVE,
    val goal: Goal = Goal.MAINTAIN_WEIGHT,
    // Calculated & stored targets
    val dailyCalorieTarget: Int = 2000,
    val dailyProteinTargetG: Int = 120,
    val dailyWaterTargetMl: Int = 2500,
    val isOnboardingComplete: Boolean = false
)