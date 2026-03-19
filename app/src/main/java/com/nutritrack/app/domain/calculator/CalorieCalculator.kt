package com.nutritrack.app.domain.calculator

import com.nutritrack.app.data.model.ActivityLevel
import com.nutritrack.app.data.model.Goal
import com.nutritrack.app.data.model.Sex
import com.nutritrack.app.data.model.UserProfile
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalorieCalculator @Inject constructor() {

    /**
     * Mifflin-St Jeor BMR formula (most accurate for general population)
     * Male:   BMR = (10 × weight kg) + (6.25 × height cm) − (5 × age) + 5
     * Female: BMR = (10 × weight kg) + (6.25 × height cm) − (5 × age) − 161
     */
    fun calculateBMR(
        weightKg: Float,
        heightCm: Float,
        age: Int,
        sex: Sex
    ): Float {
        val base = (10f * weightKg) + (6.25f * heightCm) - (5f * age)
        return if (sex == Sex.MALE) base + 5f else base - 161f
    }

    /**
     * TDEE = BMR × activity multiplier
     */
    fun calculateTDEE(bmr: Float, activityLevel: ActivityLevel): Float {
        val multiplier = when (activityLevel) {
            ActivityLevel.SEDENTARY         -> 1.2f
            ActivityLevel.LIGHTLY_ACTIVE    -> 1.375f
            ActivityLevel.MODERATELY_ACTIVE -> 1.55f
            ActivityLevel.VERY_ACTIVE       -> 1.725f
            ActivityLevel.EXTRA_ACTIVE      -> 1.9f
        }
        return bmr * multiplier
    }

    /**
     * Adjust TDEE based on goal:
     * - Lose weight:    −500 kcal/day (≈ 0.5 kg/week loss)
     * - Maintain:       no change
     * - Gain muscle:    +300 kcal/day (lean bulk)
     */
    fun calculateDailyCalorieTarget(tdee: Float, goal: Goal): Int {
        val adjusted = when (goal) {
            Goal.LOSE_WEIGHT     -> tdee - 500f
            Goal.MAINTAIN_WEIGHT -> tdee
            Goal.GAIN_MUSCLE     -> tdee + 300f
        }
        return adjusted.toInt().coerceAtLeast(1200) // never below 1200 kcal
    }

    /**
     * Convenience — calculate everything from a UserProfile
     */
    fun calculateFromProfile(profile: UserProfile): Int {
        val bmr  = calculateBMR(profile.weightKg, profile.heightCm, profile.age, profile.sex)
        val tdee = calculateTDEE(bmr, profile.activityLevel)
        return calculateDailyCalorieTarget(tdee, profile.goal)
    }
}