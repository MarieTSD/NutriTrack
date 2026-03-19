package com.nutritrack.app.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutritrack.app.data.model.SupplementLog
import com.nutritrack.app.data.model.UserProfile
import com.nutritrack.app.data.repository.FoodRepository
import com.nutritrack.app.data.repository.SupplementRepository
import com.nutritrack.app.data.repository.UserRepository
import com.nutritrack.app.data.repository.WaterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class DashboardState(
    val profile: UserProfile? = null,
    val today: LocalDate = LocalDate.now(),
    // Calories
    val totalCaloriesToday: Float = 0f,
    // Protein
    val totalProteinToday: Float = 0f,
    // Water
    val totalWaterMlToday: Int = 0,
    // Supplements
    val supplements: List<SupplementLog> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val foodRepository: FoodRepository,
    private val waterRepository: WaterRepository,
    private val supplementRepository: SupplementRepository
) : ViewModel() {

    private val today = LocalDate.now()

    val state: StateFlow<DashboardState> = combine(
        userRepository.getProfile(),
        foodRepository.getTotalCaloriesForDay(today),
        foodRepository.getTotalProteinForDay(today),
        waterRepository.getTotalForDay(today),
        supplementRepository.getSupplementsForDay(today)
    ) { profile, calories, protein, water, supplements ->
        DashboardState(
            profile             = profile,
            today               = today,
            totalCaloriesToday  = calories,
            totalProteinToday   = protein,
            totalWaterMlToday   = water,
            supplements         = supplements,
            isLoading           = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DashboardState()
    )

    fun addWater(amountMl: Int) {
        viewModelScope.launch {
            waterRepository.addWater(amountMl, today)
        }
    }

    fun toggleSupplement(log: SupplementLog) {
        viewModelScope.launch {
            supplementRepository.setTaken(log.id, !log.isTaken)
        }
    }

    fun addSupplement(name: String, dose: String) {
        viewModelScope.launch {
            supplementRepository.addSupplement(name, dose, today)
        }
    }

    fun deleteSupplement(log: SupplementLog) {
        viewModelScope.launch {
            supplementRepository.deleteSupplement(log)
        }
    }

    init {
        // Auto-copy yesterday's supplements if today has none
        viewModelScope.launch {
            supplementRepository.copyYesterdaySupplements(today)
        }
    }
}
