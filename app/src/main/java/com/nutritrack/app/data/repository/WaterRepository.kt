package com.nutritrack.app.data.repository

import com.nutritrack.app.data.db.WaterLogDao
import com.nutritrack.app.data.model.WaterLog
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WaterRepository @Inject constructor(
    private val waterLogDao: WaterLogDao
) {
    fun getTotalForDay(date: LocalDate): Flow<Int> =
        waterLogDao.getTotalForDay(date.toEpochDay())

    fun getEntriesForDay(date: LocalDate): Flow<List<WaterLog>> =
        waterLogDao.getEntriesForDay(date.toEpochDay())

    suspend fun addWater(amountMl: Int, date: LocalDate) =
        waterLogDao.insert(
            WaterLog(amountMl = amountMl, dateEpochDay = date.toEpochDay())
        )

    suspend fun deleteEntry(log: WaterLog) =
        waterLogDao.delete(log)
}
