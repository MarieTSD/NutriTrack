package com.nutritrack.app.ui.onboarding.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nutritrack.app.data.model.Goal
import com.nutritrack.app.ui.onboarding.OnboardingViewModel
import com.nutritrack.app.ui.components.OnboardingProgressBar
import com.nutritrack.app.ui.components.MacroResultCard

@Composable
fun GoalResultScreen(
    onFinish: () -> Unit,
    onBack: () -> Unit,
    viewModel: OnboardingViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Recalculate every time goal changes
    LaunchedEffect(state.goal) { viewModel.calculateTargets() }

    // Navigate when save is complete
    LaunchedEffect(state.isComplete) { if (state.isComplete) onFinish() }

    val goalOptions = mapOf(
        Goal.LOSE_WEIGHT     to Pair("Lose weight",    "−500 kcal/day deficit"),
        Goal.MAINTAIN_WEIGHT to Pair("Maintain weight","Stay at TDEE"),
        Goal.GAIN_MUSCLE     to Pair("Gain muscle",    "+300 kcal/day surplus")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        OnboardingProgressBar(currentStep = 3, totalSteps = 3)

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "Your goal",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Goal selection cards
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            goalOptions.forEach { (goal, pair) ->
                val (title, subtitle) = pair
                OutlinedCard(
                    onClick = { viewModel.setGoal(goal) },
                    border  = if (state.goal == goal)
                        androidx.compose.foundation.BorderStroke(
                            2.dp, MaterialTheme.colorScheme.primary
                        ) else CardDefaults.outlinedCardBorder(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        RadioButton(
                            selected = state.goal == goal,
                            onClick  = { viewModel.setGoal(goal) }
                        )
                        Column {
                            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                            Text(subtitle, fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        }
                    }
                }
            }
        }

        // Results
        if (state.calculatedCalories > 0) {
            Divider()
            Text(
                "Your daily targets",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                MacroResultCard(
                    label  = "Calories",
                    value  = "${state.calculatedCalories}",
                    unit   = "kcal",
                    color  = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                MacroResultCard(
                    label  = "Protein",
                    value  = "${state.calculatedProtein}",
                    unit   = "g",
                    color  = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.weight(1f)
                )
            }
            Text(
                "These targets are calculated using the Mifflin-St Jeor formula " +
                "and protein guidelines (0.8–2.0 g/kg based on your activity and goal).",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick  = viewModel::saveAndFinish,
            enabled  = !state.isSaving,
            modifier = Modifier.fillMaxWidth().height(52.dp)
        ) {
            if (state.isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Let's go!", fontSize = 16.sp)
            }
        }
    }
}
