package com.ojasx.whotouchedmyphone.RoomDb.PIN

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ojasx.whotouchedmyphone.RoomDb.Logs.IntruderLog
import com.ojasx.whotouchedmyphone.RoomDb.Logs.IntruderLogDao
import com.ojasx.whotouchedmyphone.RoomDb.SecurityAns.SecurityAnswer
import com.ojasx.whotouchedmyphone.RoomDb.SecurityAns.SecurityAnswerDao

@Database(
    entities = [
        PinEntity::class,
        IntruderLog::class,
        SecurityAnswer::class
               ],
    version = 4)
abstract class AppDatabase : RoomDatabase() {

    abstract fun pinDao(): PinDao
    abstract fun intruderLogDao(): IntruderLogDao
    abstract fun securityAnswerDao(): SecurityAnswerDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pin_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
