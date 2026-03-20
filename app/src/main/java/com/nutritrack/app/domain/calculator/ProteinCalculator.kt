package com.nutritrack.app.domain.calculator

import com.nutritrack.app.data.model.ActivityLevel
import com.nutritrack.app.data.model.Goal
import com.nutritrack.app.data.model.UserProfile
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProteinCalculator @Inject constructor() {

    /**
     * Protein calculation using lean body mass (LBM) for best accuracy.
     *
     * LBM = total weight × (1 - body fat %)
     *
     * Multipliers per goal:
     * - Lose weight (recomposition): 2.2 g/kg LBM — protects muscle in deficit
     * - Maintain:                    1.8 g/kg LBM
     * - Gain muscle:                 2.4 g/kg LBM — supports hypertrophy
     *
     * Activity modifier:
     * - Sedentary:         ×0.9  (reduce slightly)
     * - Lightly active:    ×1.0
     * - Moderately active: ×1.0
     * - Very active:       ×1.1  (increase for heavy training)
     * - Extra active:      ×1.2
     *
     * Older adults (65+): minimum 1.0 g/kg total body weight
     */
    fun calculateDailyProteinTarget(
        weightKg: Float,
        bodyFatPercent: Float,
        age: Int,
        activityLevel: ActivityLevel,
        goal: Goal
    ): Int {
        // Calculate lean body mass
        val leanMassKg = weightKg * (1f - (bodyFatPercent / 100f))

        // Base g/kg of LBM by goal
        var gPerKgLbm = when (goal) {
            Goal.LOSE_WEIGHT     -> 2.2f  // preserve muscle in deficit
            Goal.MAINTAIN_WEIGHT -> 1.8f
            Goal.GAIN_MUSCLE     -> 2.4f  // support hypertrophy
            Goal.BODY_RECOMPOSITION -> 2.4f  // high protein for recomposition
        }

        // Activity modifier
        val activityMod = when (activityLevel) {
            ActivityLevel.SEDENTARY         -> 0.9f
            ActivityLevel.LIGHTLY_ACTIVE    -> 1.0f
            ActivityLevel.MODERATELY_ACTIVE -> 1.0f
            ActivityLevel.VERY_ACTIVE       -> 1.1f
            ActivityLevel.EXTRA_ACTIVE      -> 1.2f
        }
        gPerKgLbm *= activityMod

        val calculated = leanMassKg * gPerKgLbm

        // Older adult floor: minimum 1.0 g/kg total body weight
        val floor = if (age >= 65) weightKg * 1.0f else 0f

        return calculated.coerceAtLeast(floor).toInt()
    }

    /**
     * Convenience — calculate from a UserProfile
     */
    fun calculateFromProfile(profile: UserProfile): Int =
        calculateDailyProteinTarget(
            weightKg       = profile.weightKg,
            bodyFatPercent = profile.bodyFatPercent,
            age            = profile.age,
            activityLevel  = profile.activityLevel,
            goal           = profile.goal
        )

    /**
     * Human-readable explanation shown on results screen
     */
    fun getRecommendationLabel(goal: Goal, leanMassKg: Float): String {
        val gPerKg = when (goal) {
            Goal.LOSE_WEIGHT     -> "2.2"
            Goal.MAINTAIN_WEIGHT -> "1.8"
            Goal.GAIN_MUSCLE     -> "2.4"
            Goal.BODY_RECOMPOSITION -> "2.4"
        }
        return "$gPerKg g × ${String.format("%.1f", leanMassKg)} kg lean mass"
    }
}