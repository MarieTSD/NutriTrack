package com.nutritrack.app.ui.onboarding.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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

    data class GoalOption(
        val goal: Goal,
        val title: String,
        val subtitle: String
    )

    val goalOptions = listOf(
        GoalOption(Goal.LOSE_WEIGHT,        "Lose weight",           "−500 kcal/day deficit"),
        GoalOption(Goal.MAINTAIN_WEIGHT,    "Maintain weight",       "Stay at TDEE"),
        GoalOption(Goal.GAIN_MUSCLE,        "Gain muscle",           "+300 kcal/day surplus"),
        GoalOption(Goal.BODY_RECOMPOSITION, "Body recomposition",    "Target muscle % & fat %")
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

        // ── Goal selection cards ──────────────────────────────────
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            goalOptions.forEach { option ->
                OutlinedCard(
                    onClick = { viewModel.setGoal(option.goal) },
                    border  = if (state.goal == option.goal)
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
                                Text(
                                    option.title,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 15.sp
                                )
                                Text(
                                    option.subtitle,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }

                        // ── Recomposition target inputs ───────────
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
                                    placeholder = { Text(
                                        "e.g. ${(state.muscleMassPercent.toFloatOrNull()
                                            ?.plus(5f))?.toInt() ?: 45}"
                                    )},
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Decimal
                                    ),
                                    modifier = Modifier.weight(1f),
                                    suffix = { Text("%") }
                                )
                                OutlinedTextField(
                                    value = state.targetBodyFatPercent,
                                    onValueChange = viewModel::setTargetBodyFat,
                                    label = { Text("Target fat %") },
                                    placeholder = { Text(
                                        "e.g. ${(state.bodyFatPercent.toFloatOrNull()
                                            ?.minus(5f))?.coerceAtLeast(5f)?.toInt() ?: 15}"
                                    )},
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Decimal
                                    ),
                                    modifier = Modifier.weight(1f),
                                    suffix = { Text("%") }
                                )
                            }
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Calories and protein will adjust based on how far " +
                                "you are from your targets.",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            }
        }

        // ── Results ───────────────────────────────────────────────
        if (state.calculatedCalories > 0) {
            HorizontalDivider()
            Text("Your daily targets", fontWeight = FontWeight.Bold, fontSize = 17.sp)
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                MacroResultCard(
                    label    = "Calories",
                    value    = "${state.calculatedCalories}",
                    unit     = "kcal",
                    color    = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                MacroResultCard(
                    label    = "Protein",
                    value    = "${state.calculatedProtein}",
                    unit     = "g",
                    color    = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.weight(1f)
                )
            }

            // Show recomposition context if applicable
            if (state.goal == Goal.BODY_RECOMPOSITION) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            "Body recomposition strategy",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        val currentFat    = state.bodyFatPercent.toFloatOrNull() ?: 0f
                        val targetFat     = state.targetBodyFatPercent.toFloatOrNull()
                        val currentMuscle = state.muscleMassPercent.toFloatOrNull() ?: 0f
                        val targetMuscle  = state.targetMuscleMassPercent.toFloatOrNull()

                        if (targetFat != null) {
                            Text(
                                "Fat: ${currentFat.toInt()}% → ${targetFat.toInt()}% " +
                                "(${if (currentFat > targetFat) "−${(currentFat - targetFat).toInt()}%" else "on target"})",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                        if (targetMuscle != null) {
                            Text(
                                "Muscle: ${currentMuscle.toInt()}% → ${targetMuscle.toInt()}% " +
                                "(${if (targetMuscle > currentMuscle) "+${(targetMuscle - currentMuscle).toInt()}%" else "on target"})",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                        Text(
                            "High protein intake (${state.calculatedProtein}g) preserves " +
                            "muscle while burning fat.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                        )
                    }
                }
            } else {
                Text(
                    "Calculated using Mifflin-St Jeor formula and protein guidelines " +
                    "(0.8–2.0 g/kg based on activity and goal).",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
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
