package com.nutritrack.app.ui.dashboard.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import com.nutritrack.app.data.model.SupplementLog
import kotlin.math.min

// ── Macro Ring Card ───────────────────────────────────────────────

@Composable
fun MacroRingCard(
    label: String,
    current: Float,
    target: Float,
    unit: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    val progress = if (target > 0f) min(current / target, 1f) else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 800, easing = EaseInOutCubic),
        label = "ring_$label"
    )

    Card(
        modifier = modifier,
        shape    = RoundedCornerShape(20.dp),
        colors   = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(label, fontSize = 13.sp, fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)

            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(100.dp)) {
                Canvas(modifier = Modifier.size(100.dp)) {
                    val strokeWidth = 10.dp.toPx()
                    val radius = (size.minDimension - strokeWidth) / 2
                    val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)
                    val arcSize = Size(radius * 2, radius * 2)
                    // Track
                    drawArc(
                        color       = color.copy(alpha = 0.15f),
                        startAngle  = -90f,
                        sweepAngle  = 360f,
                        useCenter   = false,
                        topLeft     = topLeft,
                        size        = arcSize,
                        style       = Stroke(strokeWidth, cap = StrokeCap.Round)
                    )
                    // Progress
                    drawArc(
                        color       = color,
                        startAngle  = -90f,
                        sweepAngle  = 360f * animatedProgress,
                        useCenter   = false,
                        topLeft     = topLeft,
                        size        = arcSize,
                        style       = Stroke(strokeWidth, cap = StrokeCap.Round)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = current.toInt().toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                    Text(
                        text = unit,
                        fontSize = 10.sp,
                        color = color.copy(alpha = 0.7f)
                    )
                }
            }

            Text(
                text = "/ ${target.toInt()} $unit",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )

            // Remaining
            val remaining = (target - current).coerceAtLeast(0f)
            Text(
                text = if (remaining == 0f) "✓ Goal reached!" else "${remaining.toInt()} $unit left",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = if (remaining == 0f) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ── Water Tracker Card ────────────────────────────────────────────

@Composable
fun WaterTrackerCard(
    currentMl: Int,
    targetMl: Int,
    onAddWater: () -> Unit
) {
    val progress = if (targetMl > 0) min(currentMl.toFloat() / targetMl, 1f) else 0f
    val waterColor = Color(0xFF29B6F6)

    Card(
        shape  = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.WaterDrop, contentDescription = null,
                        tint = waterColor, modifier = Modifier.size(20.dp))
                    Text("Water", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                }
                TextButton(onClick = onAddWater) {
                    Icon(Icons.Default.Add, contentDescription = null,
                        modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Add")
                }
            }

            LinearProgressIndicator(
                progress = { progress },
                modifier  = Modifier.fillMaxWidth().height(8.dp),
                color     = waterColor,
                trackColor = waterColor.copy(alpha = 0.15f)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${currentMl} ml",
                    fontWeight = FontWeight.Bold,
                    color = waterColor,
                    fontSize = 15.sp
                )
                Text(
                    text = "/ ${targetMl} ml",
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    fontSize = 13.sp
                )
            }

            // Quick add buttons
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(150, 250, 500).forEach { ml ->
                    OutlinedButton(
                        onClick = onAddWater,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text("+${ml}ml", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

// ── Supplements Card ──────────────────────────────────────────────

@Composable
fun SupplementsCard(
    supplements: List<SupplementLog>,
    onToggle: (SupplementLog) -> Unit,
    onDelete: (SupplementLog) -> Unit,
    onAddNew: () -> Unit
) {
    Card(
        shape  = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Medication, contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(20.dp))
                    Text("Vitamins & Supplements",
                        fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                }
                TextButton(onClick = onAddNew) {
                    Icon(Icons.Default.Add, contentDescription = null,
                        modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Add")
                }
            }

            if (supplements.isEmpty()) {
                Text(
                    text = "No supplements added for today.\nTap Add to get started.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                val takenCount = supplements.count { it.isTaken }
                Text(
                    text = "$takenCount / ${supplements.size} taken",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.tertiary
                )
                supplements.forEach { supplement ->
                    SupplementRow(
                        supplement = supplement,
                        onToggle   = { onToggle(supplement) },
                        onDelete   = { onDelete(supplement) }
                    )
                }
            }
        }
    }
}

@Composable
fun SupplementRow(
    supplement: SupplementLog,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Checkbox(
            checked  = supplement.isTaken,
            onCheckedChange = { onToggle() }
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = supplement.name,
                fontWeight = if (supplement.isTaken) FontWeight.Normal else FontWeight.Medium,
                color = if (supplement.isTaken)
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                else MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp
            )
            if (supplement.doseDescription.isNotBlank()) {
                Text(
                    text = supplement.doseDescription,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Close, contentDescription = "Delete",
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
        }
    }
}

// ── Dialogs ───────────────────────────────────────────────────────

@Composable
fun AddWaterDialog(
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var amount by remember { mutableStateOf("250") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add water") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(150, 250, 350, 500).forEach { ml ->
                        FilterChip(
                            selected = amount == ml.toString(),
                            onClick  = { amount = ml.toString() },
                            label    = { Text("${ml}ml") }
                        )
                    }
                }
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount (ml)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(amount.toIntOrNull() ?: 250) }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun AddSupplementDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var dose by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add supplement") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name (e.g. Vitamin D)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = dose,
                    onValueChange = { dose = it },
                    label = { Text("Dose (e.g. 1 capsule, 5g)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick  = { onConfirm(name, dose) },
                enabled  = name.isNotBlank()
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
