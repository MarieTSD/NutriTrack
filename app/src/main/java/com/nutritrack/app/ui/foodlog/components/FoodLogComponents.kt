package com.nutritrack.app.ui.foodlog.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nutritrack.app.data.model.FoodItem
import com.nutritrack.app.data.model.FoodLogEntry
import com.nutritrack.app.data.model.MealType
import com.nutritrack.app.ui.foodlog.FoodSearchState

// ── Daily macro summary ───────────────────────────────────────────

@Composable
fun DailyMacroSummary(
    calories: Float,
    protein: Float,
    carbs: Float,
    fat: Float
) {
    Card(
        shape  = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MacroChip("Calories", calories.toInt().toString(), "kcal",
                MaterialTheme.colorScheme.primary)
            MacroChip("Protein",  protein.toInt().toString(),  "g",
                MaterialTheme.colorScheme.secondary)
            MacroChip("Carbs",    carbs.toInt().toString(),    "g",
                MaterialTheme.colorScheme.tertiary)
            MacroChip("Fat",      fat.toInt().toString(),      "g",
                MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun MacroChip(label: String, value: String, unit: String, color: androidx.compose.ui.graphics.Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
        Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = color)
        Text(unit,  fontSize = 11.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
    }
}

// ── Meal section ──────────────────────────────────────────────────

@Composable
fun MealSection(
    mealType: MealType,
    entries: List<FoodLogEntry>,
    onAddFood: () -> Unit,
    onDeleteEntry: (FoodLogEntry) -> Unit
) {
    val mealLabel = when (mealType) {
        MealType.BREAKFAST -> "Breakfast"
        MealType.LUNCH     -> "Lunch"
        MealType.DINNER    -> "Dinner"
        MealType.SNACK     -> "Snacks"
    }
    val mealCalories = entries.sumOf { it.calories.toDouble() }.toFloat()
    val mealProtein  = entries.sumOf { it.proteinG.toDouble() }.toFloat()

    Card(
        shape  = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(mealLabel, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    if (entries.isNotEmpty()) {
                        Text(
                            "${mealCalories.toInt()} kcal · ${mealProtein.toInt()}g protein",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                TextButton(onClick = onAddFood) {
                    Icon(Icons.Default.Add, contentDescription = null,
                        modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Add food")
                }
            }

            // Entries
            if (entries.isEmpty()) {
                Text(
                    "No foods logged yet",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                entries.forEach { entry ->
                    FoodLogEntryRow(
                        entry    = entry,
                        onDelete = { onDeleteEntry(entry) }
                    )
                }
            }
        }
    }
}

@Composable
fun FoodLogEntryRow(
    entry: FoodLogEntry,
    onDelete: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(entry.foodName, fontWeight = FontWeight.Medium, fontSize = 14.sp)
            Text(
                "${entry.quantityG.toInt()}g · ${entry.calories.toInt()} kcal · " +
                "${entry.proteinG.toInt()}g protein",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
            Icon(Icons.Default.Close, contentDescription = "Remove",
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

// ── Food search bottom sheet ──────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodSearchSheet(
    searchState: FoodSearchState,
    searchResults: List<FoodItem>,
    onQueryChange: (String) -> Unit,
    onFoodSelect: (FoodItem) -> Unit,
    onMealChange: (MealType) -> Unit,
    onQtyChange: (String) -> Unit,
    onLogFood: () -> Unit,
    onDismiss: () -> Unit,
    getMacros: () -> Triple<Float, Float, Float>?
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState       = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Add food", fontWeight = FontWeight.Bold, fontSize = 18.sp)

            // Meal selector
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MealType.values().forEach { meal ->
                    val label = when (meal) {
                        MealType.BREAKFAST -> "Breakfast"
                        MealType.LUNCH     -> "Lunch"
                        MealType.DINNER    -> "Dinner"
                        MealType.SNACK     -> "Snack"
                    }
                    FilterChip(
                        selected = searchState.selectedMeal == meal,
                        onClick  = { onMealChange(meal) },
                        label    = { Text(label, fontSize = 12.sp) }
                    )
                }
            }

            if (searchState.selectedFood == null) {
                // Search field
                OutlinedTextField(
                    value         = searchState.query,
                    onValueChange = onQueryChange,
                    label         = { Text("Search food") },
                    leadingIcon   = { Icon(Icons.Default.Search, contentDescription = null) },
                    singleLine    = true,
                    modifier      = Modifier.fillMaxWidth()
                )

                // Results list
                LazyColumn(
                    modifier = Modifier.heightIn(max = 320.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(searchResults) { food ->
                        FoodSearchResultRow(
                            food     = food,
                            onClick  = { onFoodSelect(food) }
                        )
                    }
                }
            } else {
                // Food detail + quantity
                val food   = searchState.selectedFood
                val macros = getMacros()

                OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(food.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            TextButton(onClick = { onFoodSelect(food) }) {
                                Text("Change")
                            }
                        }
                        Text(
                            "Per 100g: ${food.caloriesPer100g.toInt()} kcal · " +
                            "${food.proteinPer100g}g protein · " +
                            "${food.carbsPer100g}g carbs · ${food.fatPer100g}g fat",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                OutlinedTextField(
                    value         = searchState.quantity,
                    onValueChange = onQtyChange,
                    label         = { Text("Quantity (g)") },
                    singleLine    = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier      = Modifier.fillMaxWidth(),
                    suffix        = { Text("g") }
                )

                // Live macro preview
                if (macros != null) {
                    val (cal, prot, fat) = macros
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("${cal.toInt()}", fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary)
                                Text("kcal", fontSize = 11.sp)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("${prot.toInt()}g", fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary)
                                Text("protein", fontSize = 11.sp)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("${fat.toInt()}g", fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.tertiary)
                                Text("fat", fontSize = 11.sp)
                            }
                        }
                    }
                }

                Button(
                    onClick  = onLogFood,
                    enabled  = searchState.quantity.toFloatOrNull() != null,
                    modifier = Modifier.fillMaxWidth().height(52.dp)
                ) {
                    Text("Add to ${when(searchState.selectedMeal) {
                        MealType.BREAKFAST -> "Breakfast"
                        MealType.LUNCH     -> "Lunch"
                        MealType.DINNER    -> "Dinner"
                        MealType.SNACK     -> "Snack"
                    }}", fontSize = 16.sp)
                }
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun FoodSearchResultRow(
    food: FoodItem,
    onClick: () -> Unit
) {
    OutlinedCard(
        onClick  = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(food.name, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                Text(
                    "${food.caloriesPer100g.toInt()} kcal · ${food.proteinPer100g}g protein per 100g",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(Icons.Default.Add, contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp))
        }
    }
}
