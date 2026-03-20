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
     * Katch-McArdle BMR — most accurate when body fat % is known.
     * BMR = 370 + (21.6 × lean body mass in kg)
     *
     * Falls back to Mifflin-St Jeor if body fat is 0 or unknown.
     */
    fun calculateBMR(
        weightKg: Float,
        heightCm: Float,
        age: Int,
        sex: Sex,
        bodyFatPercent: Float = 0f
    ): Float {
        return if (bodyFatPercent > 0f) {
            // Katch-McArdle (uses LBM — more accurate for body recomposition)
            val leanMassKg = weightKg * (1f - (bodyFatPercent / 100f))
            370f + (21.6f * leanMassKg)
        } else {
            // Mifflin-St Jeor fallback
            val base = (10f * weightKg) + (6.25f * heightCm) - (5f * age)
            if (sex == Sex.MALE) base + 5f else base - 161f
        }
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
     * - Maintain:        no change
     * - Gain muscle:    +300 kcal/day (lean bulk)
     */
    fun calculateDailyCalorieTarget(tdee: Float, goal: Goal): Int {
        val adjusted = when (goal) {
            Goal.LOSE_WEIGHT     -> tdee - 500f
            Goal.MAINTAIN_WEIGHT -> tdee
            Goal.GAIN_MUSCLE     -> tdee + 300f
            Goal.BODY_RECOMPOSITION -> tdee - 250f  // mild deficit for recomposition
        }
        return adjusted.toInt().coerceAtLeast(1200)
    }

    /**
     * Convenience — calculate everything from a UserProfile
     */
    fun calculateFromProfile(profile: UserProfile): Int {
        val bmr  = calculateBMR(
            weightKg       = profile.weightKg,
            heightCm       = profile.heightCm,
            age            = profile.age,
            sex            = profile.sex,
            bodyFatPercent = profile.bodyFatPercent
        )
        val tdee = calculateTDEE(bmr, profile.activityLevel)
        return calculateDailyCalorieTarget(tdee, profile.goal)
    }
}