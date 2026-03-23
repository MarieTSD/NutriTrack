package com.nutritrack.app

import android.app.Application
import com.nutritrack.app.data.db.FoodDatabaseSeeder
import com.nutritrack.app.data.db.NutriTrackDatabase
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class NutriTrackApp : Application() {

    @Inject lateinit var database: NutriTrackDatabase

    override fun onCreate() {
        super.onCreate()
        seedFoodDatabase()
    }

    private fun seedFoodDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            val dao = database.foodItemDao()
            if (dao.count() == 0) {
                dao.insertAll(FoodDatabaseSeeder.getFoods())
            }
        }
    }
}