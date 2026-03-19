package com.nutritrack.app.domain.calculator

import com.nutritrack.app.data.model.ActivityLevel
import com.nutritrack.app.data.model.Goal
import com.nutritrack.app.data.model.UserProfile
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProteinCalculator @Inject constructor() {

    /**
     * Protein guidelines (g per kg of body weight):
     *
     * Sedentary adult:          0.8  g/kg  (minimum RDA)
     * Lightly active:           1.0  g/kg
     * Moderately active:        1.4  g/kg
     * Very active / athlete:    1.8  g/kg
     * Extra active:             2.0  g/kg
     *
     * Goal modifiers:
     * - Lose weight:  +0.2 g/kg (preserve muscle in deficit)
     * - Gain muscle:  +0.2 g/kg (support hypertrophy)
     * - Maintain:     no modifier
     *
     * Older adults (65+):       minimum 1.0 g/kg to prevent muscle loss
     */
    fun calculateDailyProteinTarget(
        weightKg: Float,
        age: Int,
        activityLevel: ActivityLevel,
        goal: Goal
    ): Int {
        // Base g/kg by activity
        var gPerKg = when (activityLevel) {
            ActivityLevel.SEDENTARY         -> 0.8f
            ActivityLevel.LIGHTLY_ACTIVE    -> 1.0f
            ActivityLevel.MODERATELY_ACTIVE -> 1.4f
            ActivityLevel.VERY_ACTIVE       -> 1.8f
            ActivityLevel.EXTRA_ACTIVE      -> 2.0f
        }

        // Goal modifier
        gPerKg += when (goal) {
            Goal.LOSE_WEIGHT     -> 0.2f
            Goal.GAIN_MUSCLE     -> 0.2f
            Goal.MAINTAIN_WEIGHT -> 0.0f
        }

        // Older adult floor
        if (age >= 65) gPerKg = gPerKg.coerceAtLeast(1.0f)

        return (weightKg * gPerKg).toInt()
    }

    /**
     * Convenience — calculate from a UserProfile
     */
    fun calculateFromProfile(profile: UserProfile): Int =
        calculateDailyProteinTarget(
            weightKg      = profile.weightKg,
            age           = profile.age,
            activityLevel = profile.activityLevel,
            goal          = profile.goal
        )

    /**
     * Returns a human-readable explanation of the recommendation
     * shown on the onboarding result screen
     */
    fun getRecommendationLabel(activityLevel: ActivityLevel, age: Int): String {
        if (age >= 65) return "Older adult — minimum 1.0 g/kg to prevent muscle loss"
        return when (activityLevel) {
            ActivityLevel.SEDENTARY         -> "Sedentary adult — 0.8 g/kg (RDA minimum)"
            ActivityLevel.LIGHTLY_ACTIVE    -> "Lightly active — 1.0 g/kg"
            ActivityLevel.MODERATELY_ACTIVE -> "Moderately active — 1.4 g/kg"
            ActivityLevel.VERY_ACTIVE       -> "Active/athlete — 1.8 g/kg"
            ActivityLevel.EXTRA_ACTIVE      -> "Very active — 2.0 g/kg"
        }
    }
}