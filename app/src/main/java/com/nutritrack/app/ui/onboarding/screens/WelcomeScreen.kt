package com.nutritrack.app.ui.onboarding.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nutritrack.app.data.model.Sex
import com.nutritrack.app.ui.onboarding.OnboardingViewModel
import com.nutritrack.app.ui.components.OnboardingProgressBar

@Composable
fun WelcomeScreen(
    onNext: () -> Unit,
    viewModel: OnboardingViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        OnboardingProgressBar(currentStep = 1, totalSteps = 3)

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Welcome to NutriTrack",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Let's set up your profile to calculate your personal nutrition targets.",
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurface
        )

        // Name
        OutlinedTextField(
            value = state.name,
            onValueChange = viewModel::setName,
            label = { Text("Your name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Age
        OutlinedTextField(
            value = state.age,
            onValueChange = viewModel::setAge,
            label = { Text("Age") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        // Sex
        Text("Biological sex", fontWeight = FontWeight.Medium)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Sex.values().forEach { sex ->
                FilterChip(
                    selected = state.sex == sex,
                    onClick  = { viewModel.setSex(sex) },
                    label    = { Text(sex.name.lowercase().replaceFirstChar { it.uppercase() }) }
                )
            }
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick  = onNext,
            enabled  = state.name.isNotBlank() && (state.age.toIntOrNull() ?: 0) > 0,
            modifier = Modifier.fillMaxWidth().height(52.dp)
        ) {
            Text("Continue", fontSize = 16.sp)
        }
    }
}
