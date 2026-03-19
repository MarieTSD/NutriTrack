package com.nutritrack.app.ui.onboarding.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nutritrack.app.data.model.ActivityLevel
import com.nutritrack.app.ui.onboarding.OnboardingViewModel
import com.nutritrack.app.ui.components.OnboardingProgressBar

@Composable
fun BodyInfoScreen(
    onNext: () -> Unit,
    onBack: () -> Unit,
    viewModel: OnboardingViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val activityLabels = mapOf(
        ActivityLevel.SEDENTARY          to "Sedentary (desk job, no exercise)",
        ActivityLevel.LIGHTLY_ACTIVE     to "Lightly active (1–3 days/week)",
        ActivityLevel.MODERATELY_ACTIVE  to "Moderately active (3–5 days/week)",
        ActivityLevel.VERY_ACTIVE        to "Very active (6–7 days/week)",
        ActivityLevel.EXTRA_ACTIVE       to "Extra active (athlete / physical job)"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        OnboardingProgressBar(currentStep = 2, totalSteps = 3)

        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "Body composition",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Weight & Height side by side
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value = state.weightKg,
                onValueChange = viewModel::setWeight,
                label = { Text("Weight (kg)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = state.heightCm,
                onValueChange = viewModel::setHeight,
                label = { Text("Height (cm)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.weight(1f)
            )
        }

        // Muscle & Fat side by side
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value = state.muscleMassPercent,
                onValueChange = viewModel::setMuscleMass,
                label = { Text("Muscle mass (%)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = state.bodyFatPercent,
                onValueChange = viewModel::setBodyFat,
                label = { Text("Body fat (%)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.weight(1f)
            )
        }

        // Activity level
        Text("Activity level", fontWeight = FontWeight.Medium, fontSize = 15.sp)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            activityLabels.forEach { (level, label) ->
                OutlinedCard(
                    onClick = { viewModel.setActivityLevel(level) },
                    border  = if (state.activityLevel == level)
                        CardDefaults.outlinedCardBorder().let {
                            androidx.compose.foundation.BorderStroke(
                                2.dp, MaterialTheme.colorScheme.primary
                            )
                        } else CardDefaults.outlinedCardBorder(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        RadioButton(
                            selected = state.activityLevel == level,
                            onClick  = { viewModel.setActivityLevel(level) }
                        )
                        Text(label, fontSize = 14.sp)
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick  = onNext,
            modifier = Modifier.fillMaxWidth().height(52.dp)
        ) {
            Text("Continue", fontSize = 16.sp)
        }
    }
}
