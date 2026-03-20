package com.nutritrack.app.data.repository

import com.nutritrack.app.data.db.UserProfileDao
import com.nutritrack.app.data.model.UserProfile
import com.nutritrack.app.domain.calculator.CalorieCalculator
import com.nutritrack.app.domain.calculator.ProteinCalculator
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userProfileDao: UserProfileDao,
    private val calorieCalculator: CalorieCalculator,
    private val proteinCalculator: ProteinCalculator
) {
    fun getProfile(): Flow<UserProfile?> = userProfileDao.getProfile()

    /**
     * Save profile and automatically recalculate calorie + protein targets
     */
    suspend fun saveProfile(profile: UserProfile) {
        val calories = calorieCalculator.calculateFromProfile(profile)
        val protein  = proteinCalculator.calculateFromProfile(profile)
        userProfileDao.upsert(
            profile.copy(
                dailyCalorieTarget = calories,
                dailyProteinTargetG = protein
            )
        )
    }

    suspend fun completeOnboarding() = userProfileDao.markOnboardingComplete()

    suspend fun getProfileOnce(): UserProfile? = userProfileDao.getProfileOnce()
}
