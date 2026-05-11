package com.ojasx.whotouchedmyphone.RoomDb.SecurityAns

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SecurityAnswerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAnswer(
        securityAnswer: SecurityAnswer
    )

    @Query("SELECT * FROM security_answer WHERE id = 1")
    suspend fun getSecurityData(): SecurityAnswer?
}