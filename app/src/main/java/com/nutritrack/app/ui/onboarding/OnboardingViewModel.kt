package com.nutritrack.app.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutritrack.app.data.db.UserProfileDao
import com.nutritrack.app.data.model.*
import com.nutritrack.app.data.repository.UserRepository
import com.nutritrack.app.domain.calculator.CalorieCalculator
import com.nutritrack.app.domain.calculator.ProteinCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OnboardingState(
    // Screen 1 — pre-filled with real stats
    val name: String = "Marie",
    val age: String = "27",
    val sex: Sex = Sex.FEMALE,
    // Screen 2
    val weightKg: String = "68",
    val heightCm: String = "170",
    val muscleMassPercent: String = "32",
    val bodyFatPercent: String = "27",
    val activityLevel: ActivityLevel = ActivityLevel.VERY_ACTIVE,
    // Screen 3
    val goal: Goal = Goal.BODY_RECOMPOSITION,
    val targetMuscleMassPercent: String = "35",
    val targetBodyFatPercent: String = "20",
    // Calculated targets
    val calculatedCalories: Int = 0,
    val calculatedProtein: Int = 0,
    // Manual overrides — empty means use calculated value
    val overrideCalories: String = "",
    val overrideProtein: String = "",
    val isEditingTargets: Boolean = false,
    // Status
    val isSaving: Boolean = false,
    val isComplete: Boolean = false
) {
    val finalCalories: Int get() = overrideCalories.toIntOrNull() ?: calculatedCalories
    val finalProtein: Int  get() = overrideProtein.toIntOrNull()  ?: calculatedProtein
}

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userProfileDao: UserProfileDao,
    private val calorieCalculator: CalorieCalculator,
    private val proteinCalculator: ProteinCalculator
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state.asStateFlow()

    // ── Screen 1 ─────────────────────────────────────────────────
    fun setName(v: String)       { _state.update { it.copy(name = v) } }
    fun setAge(v: String)        { _state.update { it.copy(age = v) } }
    fun setSex(v: Sex)           { _state.update { it.copy(sex = v) } }

    // ── Screen 2 ─────────────────────────────────────────────────
    fun setWeight(v: String)     { _state.update { it.copy(weightKg = v) } }
    fun setHeight(v: String)     { _state.update { it.copy(heightCm = v) } }
    fun setMuscleMass(v: String) { _state.update { it.copy(muscleMassPercent = v) } }
    fun setBodyFat(v: String)    { _state.update { it.copy(bodyFatPercent = v) } }
    fun setActivityLevel(v: ActivityLevel) { _state.update { it.copy(activityLevel = v) } }

    // ── Screen 3 ─────────────────────────────────────────────────
    fun setGoal(v: Goal)               { _state.update { it.copy(goal = v) } }
    fun setTargetMuscleMass(v: String) { _state.update { it.copy(targetMuscleMassPercent = v) } }
    fun setTargetBodyFat(v: String)    { _state.update { it.copy(targetBodyFatPercent = v) } }

    // ── Manual overrides ──────────────────────────────────────────
    fun setOverrideCalories(v: String) { _state.update { it.copy(overrideCalories = v) } }
    fun setOverrideProtein(v: String)  { _state.update { it.copy(overrideProtein = v) } }
    fun toggleEditingTargets()         { _state.update { it.copy(isEditingTargets = !it.isEditingTargets) } }
    fun resetToCalculated() {
        _state.update { it.copy(overrideCalories = "", overrideProtein = "") }
    }

    fun calculateTargets() {
        val s = _state.value
        val profile  = buildProfile(s)
        val calories = calorieCalculator.calculateFromProfile(profile)
        val protein  = proteinCalculator.calculateFromProfile(profile)
        _state.update { it.copy(calculatedCalories = calories, calculatedProtein = protein) }
    }

    fun saveAndFinish() {
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            val s = _state.value

            // Delete old profile first to guarantee clean save
            userProfileDao.deleteProfile()

            // Save with final targets (override or calculated)
            val profile = buildProfile(s).copy(
                dailyCalorieTarget   = s.finalCalories,
                dailyProteinTargetG  = s.finalProtein,
                isOnboardingComplete = true
            )
            userRepository.saveProfile(profile)

            _state.update { it.copy(isSaving = false, isComplete = true) }
        }
    }

    private fun buildProfile(s: OnboardingState) = UserProfile(
        id                   = 1,
        name                 = s.name,
        age                  = s.age.toIntOrNull() ?: 27,
        sex                  = s.sex,
        weightKg             = s.weightKg.toFloatOrNull() ?: 68f,
        heightCm             = s.heightCm.toFloatOrNull() ?: 170f,
        muscleMassPercent    = s.muscleMassPercent.toFloatOrNull() ?: 32f,
        bodyFatPercent       = s.bodyFatPercent.toFloatOrNull() ?: 27f,
        activityLevel        = s.activityLevel,
        goal                 = s.goal,
        isOnboardingComplete = false
    )
}