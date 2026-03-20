package com.nutritrack.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ActivityLevel {
    SEDENTARY,
    LIGHTLY_ACTIVE,
    MODERATELY_ACTIVE,
    VERY_ACTIVE,
    EXTRA_ACTIVE
}

enum class Goal {
    LOSE_WEIGHT,
    MAINTAIN_WEIGHT,
    GAIN_MUSCLE,
    BODY_RECOMPOSITION  // Target specific muscle % and fat %
}

enum class Sex {
    MALE,
    FEMALE
}

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val name: String = "",
    val age: Int = 25,
    val sex: Sex = Sex.MALE,
    val weightKg: Float = 70f,
    val heightCm: Float = 170f,
    val muscleMassPercent: Float = 40f,
    val bodyFatPercent: Float = 20f,
    val activityLevel: ActivityLevel = ActivityLevel.MODERATELY_ACTIVE,
    val goal: Goal = Goal.MAINTAIN_WEIGHT,
    // Body recomposition targets
    val targetMuscleMassPercent: Float? = null,  // null = not set
    val targetBodyFatPercent: Float? = null,     // null = not set
    // Calculated targets
    val dailyCalorieTarget: Int = 2000,
    val dailyProteinTargetG: Int = 120,
    val dailyWaterTargetMl: Int = 2500,
    val isOnboardingComplete: Boolean = false
)
