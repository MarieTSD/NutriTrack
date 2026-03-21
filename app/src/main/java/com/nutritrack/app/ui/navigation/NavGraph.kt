package com.nutritrack.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.nutritrack.app.ui.dashboard.DashboardScreen
import com.nutritrack.app.ui.foodlog.FoodLogScreen
import com.nutritrack.app.ui.onboarding.OnboardingViewModel
import com.nutritrack.app.ui.onboarding.screens.*

sealed class Screen(val route: String) {
    object Welcome     : Screen("welcome")
    object BodyInfo    : Screen("body_info")
    object GoalResult  : Screen("goal_result")
    object Dashboard   : Screen("dashboard")
    object FoodLog     : Screen("food_log")
    object Water       : Screen("water")
    object Supplements : Screen("supplements")
}

data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Dashboard,   "Dashboard",  Icons.Default.Home),
    BottomNavItem(Screen.FoodLog,     "Food log",   Icons.Default.Restaurant),
    BottomNavItem(Screen.Water,       "Water",      Icons.Default.WaterDrop),
    BottomNavItem(Screen.Supplements, "Supplements",Icons.Default.Medication)
)

// Routes that show the bottom bar
private val bottomBarRoutes = setOf(
    Screen.Dashboard.route,
    Screen.FoodLog.route,
    Screen.Water.route,
    Screen.Supplements.route
)

@Composable
fun NutriTrackNavGraph(
    navController: NavHostController,
    startDestination: String
) {
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route
    val showBottomBar = currentRoute in bottomBarRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.screen.route,
                            onClick  = {
                                navController.navigate(item.screen.route) {
                                    popUpTo(Screen.Dashboard.route) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon  = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController    = navController,
            startDestination = startDestination,
            modifier         = Modifier.padding(innerPadding)
        ) {
            // ── Onboarding ─────────────────────────────────────────
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

            // ── Main app ───────────────────────────────────────────
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    onNavigateToFoodLog = {
                        navController.navigate(Screen.FoodLog.route)
                    }
                )
            }
            composable(Screen.FoodLog.route) {
                FoodLogScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Water.route) {
                // Placeholder — will be a dedicated water screen
                DashboardScreen(
                    onNavigateToFoodLog = {
                        navController.navigate(Screen.FoodLog.route)
                    }
                )
            }
            composable(Screen.Supplements.route) {
                // Placeholder — will be a dedicated supplements screen
                DashboardScreen(
                    onNavigateToFoodLog = {
                        navController.navigate(Screen.FoodLog.route)
                    }
                )
            }
        }
    }
}