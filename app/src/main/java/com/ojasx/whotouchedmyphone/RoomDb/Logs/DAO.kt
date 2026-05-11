package com.ojasx.whotouchedmyphone.RoomDb.Logs

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface IntruderLogDao {

    // Insert new intruder log
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: IntruderLog)

    // Get all logs sorted by latest first
    @Query("SELECT * FROM intruder_logs ORDER BY timestamp DESC")
    fun getAllLogs(): kotlinx.coroutines.flow.Flow<List<IntruderLog>>

    // Delete all logs (optional but useful for settings screen)
    @Query("DELETE FROM intruder_logs")
    suspend fun deleteAllLogs()

    // Delete single log by id (optional)
    @Query("DELETE FROM intruder_logs WHERE id = :id")
    suspend fun deleteLogById(id: Int)
}

