package com.ojasx.whotouchedmyphone.RoomDb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePin(pin: PinEntity)

    @Query("SELECT * FROM pin_table WHERE id = 0")
    suspend fun getPin(): PinEntity?

    @Query("DELETE FROM pin_table")
    suspend fun deletePin()
}