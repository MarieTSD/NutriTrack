package com.nutritrack.app.data.repository

import com.nutritrack.app.data.db.SupplementLogDao
import com.nutritrack.app.data.model.SupplementLog
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupplementRepository @Inject constructor(
    private val supplementLogDao: SupplementLogDao
) {
    fun getSupplementsForDay(date: LocalDate): Flow<List<SupplementLog>> =
        supplementLogDao.getSupplementsForDay(date.toEpochDay())

    suspend fun addSupplement(name: String, dose: String, date: LocalDate) =
        supplementLogDao.upsert(
            SupplementLog(
                name            = name,
                doseDescription = dose,
                isTaken         = false,
                dateEpochDay    = date.toEpochDay()
            )
        )

    suspend fun setTaken(id: Int, taken: Boolean) =
        supplementLogDao.setTaken(id, taken)

    suspend fun deleteSupplement(log: SupplementLog) =
        supplementLogDao.delete(log)

    /**
     * Copy yesterday's supplement list to today (unticked)
     * so user doesn't have to re-add every day
     */
    suspend fun copyYesterdaySupplements(today: LocalDate) {
        val yesterday = today.minusDays(1)
        supplementLogDao.copySupplementsToDay(
            sourceDay = yesterday.toEpochDay(),
            targetDay = today.toEpochDay(),
            now       = System.currentTimeMillis()
        )
    }
}
