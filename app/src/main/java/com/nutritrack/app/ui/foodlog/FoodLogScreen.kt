package com.nutritrack.app.ui.foodlog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nutritrack.app.data.model.MealType
import com.nutritrack.app.ui.foodlog.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodLogScreen(
    onNavigateBack: () -> Unit,
    viewModel: FoodLogViewModel = hiltViewModel()
) {
    val logState    by viewModel.logState.collectAsStateWithLifecycle()
    val searchState by viewModel.searchState.collectAsStateWithLifecycle()
    val results     by viewModel.searchResults.collectAsStateWithLifecycle()

    var showSearchSheet by remember { mutableStateOf(false) }
    var activeMeal      by remember { mutableStateOf(MealType.BREAKFAST) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Food log", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ── Daily macro summary bar ───────────────────────────
            DailyMacroSummary(
                calories = logState.totalCalories,
                protein  = logState.totalProtein,
                carbs    = logState.totalCarbs,
                fat      = logState.totalFat
            )

            // ── Meal sections ─────────────────────────────────────
            MealType.values().forEach { meal ->
                MealSection(
                    mealType = meal,
                    entries  = logState.entriesByMeal[meal] ?: emptyList(),
                    onAddFood = {
                        activeMeal = meal
                        viewModel.setMeal(meal)
                        showSearchSheet = true
                    },
                    onDeleteEntry = viewModel::deleteEntry
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }

    // ── Food search bottom sheet ──────────────────────────────────
    if (showSearchSheet) {
        FoodSearchSheet(
            searchState   = searchState,
            searchResults = results,
            onQueryChange = viewModel::setQuery,
            onFoodSelect  = viewModel::selectFood,
            onMealChange  = viewModel::setMeal,
            onQtyChange   = viewModel::setQuantity,
            onLogFood     = {
                viewModel.logFood()
                showSearchSheet = false
            },
            onDismiss     = {
                showSearchSheet = false
                viewModel.clearSelectedFood()
            },
            getMacros     = viewModel::getCalculatedMacros
        )
    }
}
