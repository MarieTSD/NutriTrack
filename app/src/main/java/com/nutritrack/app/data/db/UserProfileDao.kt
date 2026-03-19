package com.nutritrack.app.data.db

import androidx.room.*
import com.nutritrack.app.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getProfile(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile WHERE id = 1")
    suspend fun getProfileOnce(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(profile: UserProfile)

    @Query("UPDATE user_profile SET isOnboardingComplete = 1 WHERE id = 1")
    suspend fun markOnboardingComplete()
}