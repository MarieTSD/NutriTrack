package com.nutritrack.app.ui.foodlog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutritrack.app.data.model.FoodItem
import com.nutritrack.app.data.model.FoodLogEntry
import com.nutritrack.app.data.model.MealType
import com.nutritrack.app.data.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class FoodLogState(
    val today: LocalDate = LocalDate.now(),
    val entriesByMeal: Map<MealType, List<FoodLogEntry>> = emptyMap(),
    val totalCalories: Float = 0f,
    val totalProtein: Float = 0f,
    val totalCarbs: Float = 0f,
    val totalFat: Float = 0f,
    val isLoading: Boolean = true
)

data class FoodSearchState(
    val query: String = "",
    val results: List<FoodItem> = emptyList(),
    val selectedFood: FoodItem? = null,
    val selectedMeal: MealType = MealType.BREAKFAST,
    val quantity: String = "100",
    val isSearching: Boolean = false
)

@HiltViewModel
class FoodLogViewModel @Inject constructor(
    private val foodRepository: FoodRepository
) : ViewModel() {

    private val today = LocalDate.now()

    // ── Food log state ────────────────────────────────────────────
    val logState: StateFlow<FoodLogState> = foodRepository
        .getEntriesForDay(today)
        .map { entries ->
            val byMeal = MealType.values().associateWith { meal ->
                entries.filter { it.mealType == meal }
            }
            FoodLogState(
                today         = today,
                entriesByMeal = byMeal,
                totalCalories = entries.sumOf { it.calories.toDouble() }.toFloat(),
                totalProtein  = entries.sumOf { it.proteinG.toDouble() }.toFloat(),
                totalCarbs    = entries.sumOf { it.carbsG.toDouble() }.toFloat(),
                totalFat      = entries.sumOf { it.fatG.toDouble() }.toFloat(),
                isLoading     = false
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FoodLogState()
        )

    // ── Search state ──────────────────────────────────────────────
    private val _searchState = MutableStateFlow(FoodSearchState())
    val searchState: StateFlow<FoodSearchState> = _searchState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResults: StateFlow<List<FoodItem>> = _searchState
        .map { it.query }
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isBlank()) foodRepository.getAllFoods()
            else foodRepository.searchFoods(query)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun setQuery(query: String) {
        _searchState.update { it.copy(query = query) }
    }

    fun selectFood(food: FoodItem) {
        _searchState.update { it.copy(selectedFood = food, quantity = "100") }
    }

    fun clearSelectedFood() {
        _searchState.update { it.copy(selectedFood = null, quantity = "100") }
    }

    fun setMeal(meal: MealType) {
        _searchState.update { it.copy(selectedMeal = meal) }
    }

    fun setQuantity(quantity: String) {
        _searchState.update { it.copy(quantity = quantity) }
    }

    fun logFood() {
        val s = _searchState.value
        val food = s.selectedFood ?: return
        val qty  = s.quantity.toFloatOrNull() ?: return

        viewModelScope.launch {
            foodRepository.logFood(
                foodItem  = food,
                quantityG = qty,
                meal      = s.selectedMeal,
                date      = today
            )
            _searchState.update { it.copy(selectedFood = null, query = "", quantity = "100") }
        }
    }

    fun deleteEntry(entry: FoodLogEntry) {
        viewModelScope.launch {
            foodRepository.deleteLogEntry(entry)
        }
    }

    // ── Calculated macros for selected food + quantity ────────────
    fun getCalculatedMacros(): Triple<Float, Float, Float>? {
        val s    = _searchState.value
        val food = s.selectedFood ?: return null
        val qty  = s.quantity.toFloatOrNull() ?: return null
        val ratio = qty / 100f
        return Triple(
            food.caloriesPer100g * ratio,
            food.proteinPer100g  * ratio,
            food.fatPer100g      * ratio
        )
    }
}
