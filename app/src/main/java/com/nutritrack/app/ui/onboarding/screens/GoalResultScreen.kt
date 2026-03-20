package com.nutritrack.app.ui.onboarding.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nutritrack.app.data.model.Goal
import com.nutritrack.app.ui.components.MacroResultCard
import com.nutritrack.app.ui.components.OnboardingProgressBar
import com.nutritrack.app.ui.onboarding.OnboardingViewModel

private data class GoalOption(
    val goal: Goal,
    val title: String,
    val subtitle: String
)

private val goalOptions = listOf(
    GoalOption(Goal.LOSE_WEIGHT,        "Lose weight",        "−500 kcal/day deficit"),
    GoalOption(Goal.MAINTAIN_WEIGHT,    "Maintain weight",    "Stay at TDEE"),
    GoalOption(Goal.GAIN_MUSCLE,        "Gain muscle",        "+300 kcal/day surplus"),
    GoalOption(Goal.BODY_RECOMPOSITION, "Body recomposition", "Lose fat + build muscle")
)

@Composable
fun GoalResultScreen(
    onFinish: () -> Unit,
    onBack: () -> Unit,
    viewModel: OnboardingViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.goal, state.targetBodyFatPercent, state.targetMuscleMassPercent) {
        viewModel.calculateTargets()
    }
    LaunchedEffect(state.isComplete) { if (state.isComplete) onFinish() }

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

        // ── Goal selection cards ──────────────────────────────────
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            goalOptions.forEach { option ->
                OutlinedCard(
                    onClick  = { viewModel.setGoal(option.goal) },
                    border   = if (state.goal == option.goal)
                        androidx.compose.foundation.BorderStroke(
                            2.dp, MaterialTheme.colorScheme.primary
                        ) else CardDefaults.outlinedCardBorder(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            RadioButton(
                                selected = state.goal == option.goal,
                                onClick  = { viewModel.setGoal(option.goal) }
                            )
                            Column {
                                Text(option.title, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                                Text(
                                    option.subtitle, fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }
                        if (option.goal == Goal.BODY_RECOMPOSITION &&
                            state.goal == Goal.BODY_RECOMPOSITION) {
                            Spacer(Modifier.height(12.dp))
                            HorizontalDivider(modifier = Modifier.padding(bottom = 12.dp))
                            Text(
                                "Set your body composition targets",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                OutlinedTextField(
                                    value = state.targetMuscleMassPercent,
                                    onValueChange = viewModel::setTargetMuscleMass,
                                    label = { Text("Target muscle %") },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.weight(1f),
                                    suffix = { Text("%") }
                                )
                                OutlinedTextField(
                                    value = state.targetBodyFatPercent,
                                    onValueChange = viewModel::setTargetBodyFat,
                                    label = { Text("Target fat %") },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.weight(1f),
                                    suffix = { Text("%") }
                                )
                            }
                        }
                    }
                }
            }
        }

        // ── Results ───────────────────────────────────────────────
        if (state.calculatedCalories > 0) {
            HorizontalDivider()

            // Header with edit toggle
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Your daily targets", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Row {
                    if (state.isEditingTargets) {
                        IconButton(onClick = { viewModel.resetToCalculated() }) {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = "Reset to calculated",
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                    IconButton(onClick = { viewModel.toggleEditingTargets() }) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit targets",
                            tint = if (state.isEditingTargets)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
            }

            // ── Always show the result cards (live update) ────────
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                MacroResultCard(
                    label    = "Calories",
                    value    = "${state.finalCalories}",
                    unit     = "kcal",
                    color    = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                MacroResultCard(
                    label    = "Protein",
                    value    = "${state.finalProtein}",
                    unit     = "g",
                    color    = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.weight(1f)
                )
            }

            // ── Edit fields (shown when editing) ──────────────────
            if (state.isEditingTargets) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "Override recommended targets",
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedTextField(
                                value = state.overrideCalories.ifBlank {
                                    state.calculatedCalories.toString()
                                },
                                onValueChange = viewModel::setOverrideCalories,
                                label = { Text("Calories") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number
                                ),
                                modifier = Modifier.weight(1f),
                                suffix = { Text("kcal") }
                            )
                            OutlinedTextField(
                                value = state.overrideProtein.ifBlank {
                                    state.calculatedProtein.toString()
                                },
                                onValueChange = viewModel::setOverrideProtein,
                                label = { Text("Protein") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number
                                ),
                                modifier = Modifier.weight(1f),
                                suffix = { Text("g") }
                            )
                        }
                        Text(
                            "Cards above update live as you type. Tap refresh to restore.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                    }
                }
            } else {
                if (state.overrideCalories.isNotBlank() || state.overrideProtein.isNotBlank()) {
                    Text(
                        "Custom targets set. Tap the edit icon to adjust.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                } else {
                    Text(
                        text = if (state.goal == Goal.BODY_RECOMPOSITION)
                            "High protein preserves muscle while burning fat. Tap edit to override."
                        else
                            "Calculated using Katch-McArdle formula. Tap edit to override.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
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