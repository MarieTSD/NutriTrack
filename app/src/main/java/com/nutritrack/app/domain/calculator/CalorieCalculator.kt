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
     * Mifflin-St Jeor BMR formula
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
     * - Lose weight:         −500 kcal/day
     * - Maintain:            no change
     * - Gain muscle:         +300 kcal/day (lean bulk)
     * - Body recomposition:  TDEE (maintenance) — body recomp works at maintenance
     *   with a slight lean towards deficit if fat% is far from target,
     *   or slight surplus if muscle% is far from target
     */
    fun calculateDailyCalorieTarget(
        tdee: Float,
        goal: Goal,
        currentBodyFatPercent: Float = 0f,
        targetBodyFatPercent: Float? = null,
        currentMuscleMassPercent: Float = 0f,
        targetMuscleMassPercent: Float? = null
    ): Int {
        val adjusted = when (goal) {
            Goal.LOSE_WEIGHT         -> tdee - 500f
            Goal.MAINTAIN_WEIGHT     -> tdee
            Goal.GAIN_MUSCLE         -> tdee + 300f
            Goal.BODY_RECOMPOSITION  -> {
                val fatGap    = targetBodyFatPercent?.let { currentBodyFatPercent - it } ?: 0f
                val muscleGap = targetMuscleMassPercent?.let { it - currentMuscleMassPercent } ?: 0f
                when {
                    // Fat loss is the priority (fat% is significantly above target)
                    fatGap > 5f    -> tdee - 250f
                    // Muscle gain is the priority (muscle% is significantly below target)
                    muscleGap > 5f -> tdee + 150f
                    // Close to targets — pure maintenance
                    else           -> tdee
                }
            }
        }
        return adjusted.toInt().coerceAtLeast(1200)
    }

    fun calculateFromProfile(profile: UserProfile): Int {
        val bmr  = calculateBMR(profile.weightKg, profile.heightCm, profile.age, profile.sex)
        val tdee = calculateTDEE(bmr, profile.activityLevel)
        return calculateDailyCalorieTarget(
            tdee                    = tdee,
            goal                    = profile.goal,
            currentBodyFatPercent   = profile.bodyFatPercent,
            targetBodyFatPercent    = profile.targetBodyFatPercent,
            currentMuscleMassPercent = profile.muscleMassPercent,
            targetMuscleMassPercent = profile.targetMuscleMassPercent
        )
    }
}
