package com.nutritrack.app.di

import android.content.Context
import androidx.room.Room
import com.nutritrack.app.data.db.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NutriTrackDatabase =
        Room.databaseBuilder(
            context,
            NutriTrackDatabase::class.java,
            "nutritrack.db"
        ).build()

    @Provides fun provideUserProfileDao(db: NutriTrackDatabase)  = db.userProfileDao()
    @Provides fun provideFoodItemDao(db: NutriTrackDatabase)      = db.foodItemDao()
    @Provides fun provideFoodLogDao(db: NutriTrackDatabase)       = db.foodLogDao()
    @Provides fun provideWaterLogDao(db: NutriTrackDatabase)      = db.waterLogDao()
    @Provides fun provideSupplementLogDao(db: NutriTrackDatabase) = db.supplementLogDao()
}