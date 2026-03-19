package com.nutritrack.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.nutritrack.app.ui.navigation.NutriTrackNavGraph
import com.nutritrack.app.ui.navigation.Screen
import com.nutritrack.app.ui.onboarding.OnboardingViewModel
import com.nutritrack.app.ui.theme.NutriTrackTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NutriTrackTheme {
                val navController = rememberNavController()
                val onboardingVm: OnboardingViewModel = hiltViewModel()

                var startDestination by remember { mutableStateOf<String?>(null) }

                LaunchedEffect(Unit) {
                    startDestination = Screen.Welcome.route
                }

                startDestination?.let { start ->
                    NutriTrackNavGraph(
                        navController    = navController,
                        startDestination = start
                    )
                }
            }
        }
    }
}