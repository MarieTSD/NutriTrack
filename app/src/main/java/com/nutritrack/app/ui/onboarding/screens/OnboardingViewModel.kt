package com.nutritrack.app.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutritrack.app.data.model.*
import com.nutritrack.app.data.repository.UserRepository
import com.nutritrack.app.domain.calculator.CalorieCalculator
import com.nutritrack.app.domain.calculator.ProteinCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OnboardingState(
    // Screen 1
    val name: String = "",
    val age: String = "25",
    val sex: Sex = Sex.MALE,
    // Screen 2
    val weightKg: String = "70",
    val heightCm: String = "170",
    val muscleMassPercent: String = "40",
    val bodyFatPercent: String = "20",
    val activityLevel: ActivityLevel = ActivityLevel.MODERATELY_ACTIVE,
    // Screen 3
    val goal: Goal = Goal.MAINTAIN_WEIGHT,
    // Results
    val calculatedCalories: Int = 0,
    val calculatedProtein: Int = 0,
    val isSaving: Boolean = false,
    val isComplete: Boolean = false
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val calorieCalculator: CalorieCalculator,
    private val proteinCalculator: ProteinCalculator
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state.asStateFlow()

    // ── Screen 1 ─────────────────────────────────────────────────
    fun setName(v: String)          { _state.update { it.copy(name = v) } }
    fun setAge(v: String)           { _state.update { it.copy(age = v) } }
    fun setSex(v: Sex)              { _state.update { it.copy(sex = v) } }

    // ── Screen 2 ─────────────────────────────────────────────────
    fun setWeight(v: String)        { _state.update { it.copy(weightKg = v) } }
    fun setHeight(v: String)        { _state.update { it.copy(heightCm = v) } }
    fun setMuscleMass(v: String)    { _state.update { it.copy(muscleMassPercent = v) } }
    fun setBodyFat(v: String)       { _state.update { it.copy(bodyFatPercent = v) } }
    fun setActivityLevel(v: ActivityLevel) { _state.update { it.copy(activityLevel = v) } }

    // ── Screen 3 ─────────────────────────────────────────────────
    fun setGoal(v: Goal)            { _state.update { it.copy(goal = v) } }

    /**
     * Called when user lands on GoalResult screen — calculates targets live
     */
    fun calculateTargets() {
        val s = _state.value
        val profile = buildProfile(s)
        val calories = calorieCalculator.calculateFromProfile(profile)
        val protein  = proteinCalculator.calculateFromProfile(profile)
        _state.update { it.copy(calculatedCalories = calories, calculatedProtein = protein) }
    }

    /**
     * Save profile to Room and mark onboarding complete
     */
    fun saveAndFinish() {
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            val profile = buildProfile(_state.value)
            userRepository.saveProfile(profile)
            userRepository.completeOnboarding()
            _state.update { it.copy(isSaving = false, isComplete = true) }
        }
    }

    private fun buildProfile(s: OnboardingState) = UserProfile(
        id                  = 1,
        name                = s.name,
        age                 = s.age.toIntOrNull() ?: 25,
        sex                 = s.sex,
        weightKg            = s.weightKg.toFloatOrNull() ?: 70f,
        heightCm            = s.heightCm.toFloatOrNull() ?: 170f,
        muscleMassPercent   = s.muscleMassPercent.toFloatOrNull() ?: 40f,
        bodyFatPercent      = s.bodyFatPercent.toFloatOrNull() ?: 20f,
        activityLevel       = s.activityLevel,
        goal                = s.goal,
        isOnboardingComplete = false
    )
}
