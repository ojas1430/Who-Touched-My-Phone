package com.ojasx.whotouchedmyphone.RoomDb.Logs

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "intruder_logs")
data class IntruderLog(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val imageUri: String,

    val appPackage: String,

    val appName: String,

    val timestamp: Long
)