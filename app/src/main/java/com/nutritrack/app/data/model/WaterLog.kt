package com.nutritrack.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "water_log")
data class WaterLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amountMl: Int,
    val dateEpochDay: Long,
    val timestampMs: Long = System.currentTimeMillis()
)
