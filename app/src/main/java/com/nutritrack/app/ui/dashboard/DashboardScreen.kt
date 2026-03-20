package com.nutritrack.app.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nutritrack.app.ui.dashboard.components.*
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToFoodLog: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showAddSupplementDialog by remember { mutableStateOf(false) }
    var showAddWaterDialog by remember { mutableStateOf(false) }

    if (state.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val profile = state.profile

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToFoodLog,
                icon    = { Icon(Icons.Default.Restaurant, contentDescription = null) },
                text    = { Text("Log food") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // ── Header ─────────────────────────────────────────────
            Column {
                Text(
                    text = "Hello, ${profile?.name?.ifBlank { "there" } ?: "there"}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = state.today.format(
                        DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
                    ),
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }

            // ── Calorie + Protein rings ────────────────────────────
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                MacroRingCard(
                    label   = "Calories",
                    current = state.totalCaloriesToday,
                    target  = profile?.dailyCalorieTarget?.toFloat() ?: 2000f,
                    unit    = "kcal",
                    color   = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                MacroRingCard(
                    label   = "Protein",
                    current = state.totalProteinToday,
                    target  = profile?.dailyProteinTargetG?.toFloat() ?: 120f,
                    unit    = "g",
                    color   = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.weight(1f)
                )
            }

            // ── Water tracker ─────────────────────────────────────
            WaterTrackerCard(
                currentMl = state.totalWaterMlToday,
                targetMl  = profile?.dailyWaterTargetMl ?: 2500,
                onAddWater = { showAddWaterDialog = true }
            )

            // ── Supplements ───────────────────────────────────────
            SupplementsCard(
                supplements   = state.supplements,
                onToggle      = viewModel::toggleSupplement,
                onDelete      = viewModel::deleteSupplement,
                onAddNew      = { showAddSupplementDialog = true }
            )

            Spacer(Modifier.height(80.dp)) // FAB clearance
        }
    }

    // ── Add water dialog ──────────────────────────────────────────
    if (showAddWaterDialog) {
        AddWaterDialog(
            onDismiss = { showAddWaterDialog = false },
            onConfirm = { ml ->
                viewModel.addWater(ml)
                showAddWaterDialog = false
            }
        )
    }

    // ── Add supplement dialog ─────────────────────────────────────
    if (showAddSupplementDialog) {
        AddSupplementDialog(
            onDismiss = { showAddSupplementDialog = false },
            onConfirm = { name, dose ->
                viewModel.addSupplement(name, dose)
                showAddSupplementDialog = false
            }
        )
    }
}
