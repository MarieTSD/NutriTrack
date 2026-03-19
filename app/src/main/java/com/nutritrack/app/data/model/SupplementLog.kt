package com.nutritrack.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "supplement_log")
data class SupplementLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,           // e.g. "Vitamin D", "Creatine", "Omega-3"
    val doseDescription: String = "", // e.g. "1 capsule", "5g"
    val isTaken: Boolean = false,
    val dateEpochDay: Long,
    val timestampMs: Long = System.currentTimeMillis()
)