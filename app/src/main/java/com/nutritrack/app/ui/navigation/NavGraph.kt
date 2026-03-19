package com.nutritrack.app.ui.navigation

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.nutritrack.app.ui.dashboard.DashboardScreen
import com.nutritrack.app.ui.onboarding.OnboardingViewModel
import com.nutritrack.app.ui.onboarding.screens.*

sealed class Screen(val route: String) {
    // Onboarding
    object Welcome     : Screen("welcome")
    object BodyInfo    : Screen("body_info")
    object GoalResult  : Screen("goal_result")
    // Main app
    object Dashboard   : Screen("dashboard")
    object FoodLog     : Screen("food_log")
    object FoodSearch  : Screen("food_search")
    object Water       : Screen("water")
    object Supplements : Screen("supplements")
}

@Composable
fun NutriTrackNavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(navController = navController, startDestination = startDestination) {

        // ── Onboarding ─────────────────────────────────────────────
        composable(Screen.Welcome.route) {
            val vm: OnboardingViewModel = hiltViewModel()
            WelcomeScreen(
                onNext    = { navController.navigate(Screen.BodyInfo.route) },
                viewModel = vm
            )
        }

        composable(Screen.BodyInfo.route) {
            val vm: OnboardingViewModel = hiltViewModel()
            BodyInfoScreen(
                onNext    = { navController.navigate(Screen.GoalResult.route) },
                onBack    = { navController.popBackStack() },
                viewModel = vm
            )
        }

        composable(Screen.GoalResult.route) {
            val vm: OnboardingViewModel = hiltViewModel()
            GoalResultScreen(
                onFinish  = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
                onBack    = { navController.popBackStack() },
                viewModel = vm
            )
        }

        // ── Main app ───────────────────────────────────────────────
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToFoodLog = {
                    navController.navigate(Screen.FoodLog.route)
                }
            )
        }

        // Placeholders — filled in next steps
        composable(Screen.FoodLog.route) { }
        composable(Screen.FoodSearch.route) { }
    }
}
