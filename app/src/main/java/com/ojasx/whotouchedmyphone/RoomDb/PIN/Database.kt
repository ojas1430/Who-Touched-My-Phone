package com.ojasx.whotouchedmyphone.RoomDb.PIN

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ojasx.whotouchedmyphone.RoomDb.Logs.IntruderLog
import com.ojasx.whotouchedmyphone.RoomDb.Logs.IntruderLogDao

@Database(entities = [PinEntity::class, IntruderLog::class], version = 3)
abstract class AppDatabase : RoomDatabase() {

    abstract fun pinDao(): PinDao
    abstract fun intruderLogDao(): IntruderLogDao

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
