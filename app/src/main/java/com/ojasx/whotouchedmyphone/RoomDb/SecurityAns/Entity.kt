package com.ojasx.whotouchedmyphone.RoomDb.SecurityAns

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "security_answer")
data class SecurityAnswer(

    @PrimaryKey
    val id: Int = 1,

    val question: String,

    val answer: String
)