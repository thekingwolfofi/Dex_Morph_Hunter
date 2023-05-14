package com.king.dexmorphhunter.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.king.dexmorphhunter.model.data.*
import com.king.dexmorphhunter.model.util.Converters
import javax.inject.Singleton

@Database(entities = [AppSettings::class, AppInfo::class, ClassInfo::class, MethodInfo::class, ArgumentInfo::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
@Singleton
abstract class AppDatabase : RoomDatabase() {
    abstract fun appSettingsDao(): AppSettingsDao
    abstract fun appInfoDao(): AppInfoDao
    abstract fun argumentInfoDao(): ArgumentInfoDao
    abstract fun classInfoDao(): ClassInfoDao
    abstract fun methodInfoDao(): MethodInfoDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
