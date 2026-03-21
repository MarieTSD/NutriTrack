package com.nutritrack.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.nutritrack.app.data.db.UserProfileDao
import com.nutritrack.app.ui.navigation.NutriTrackNavGraph
import com.nutritrack.app.ui.navigation.Screen
import com.nutritrack.app.ui.theme.NutriTrackTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var userProfileDao: UserProfileDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Check if onboarding is complete — done synchronously before UI renders
        val startDestination = runBlocking {
            val profile = userProfileDao.getProfileOnce()
            if (profile?.isOnboardingComplete == true) {
                Screen.Dashboard.route
            } else {
                Screen.Welcome.route
            }
        }

        setContent {
            NutriTrackTheme {
                val navController = rememberNavController()
                NutriTrackNavGraph(
                    navController    = navController,
                    startDestination = startDestination
                )
            }
        }
    }
}