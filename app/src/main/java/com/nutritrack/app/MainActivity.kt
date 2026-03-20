package com.nutritrack.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.nutritrack.app.ui.navigation.NutriTrackNavGraph
import com.nutritrack.app.ui.navigation.Screen
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
                NutriTrackNavGraph(
                    navController    = navController,
                    startDestination = Screen.Welcome.route
                )
            }
        }
    }
}
