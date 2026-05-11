package com.ojasx.whotouchedmyphone.RoomDb.PIN

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pin_table")
data class PinEntity(
    @PrimaryKey
    val id: Int = 0,
    val pinHash: String
)


