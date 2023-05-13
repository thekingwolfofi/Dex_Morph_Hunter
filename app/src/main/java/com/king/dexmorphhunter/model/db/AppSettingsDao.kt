package com.king.dexmorphhunter.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.king.dexmorphhunter.model.data.AppSettings

@Dao
interface AppSettingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateAppSettings(appSettings: AppSettings)

    @Query("SELECT * FROM app_settings LIMIT 1")
    fun getAppSettings(): AppSettings?

    @Query("DELETE FROM app_settings")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM app_settings")
    fun isAppSettingsTableEmpty(): Int

    @Query("UPDATE app_settings SET intercepted_Apps_Switch = :isIntercepted WHERE id = 1")
    fun updateInterceptedAppsSwitch(isIntercepted: Boolean)

    @Query("UPDATE app_settings SET system_Apps_Switch = :isSystemApp WHERE id = 1")
    fun updateSystemAppsSwitch(isSystemApp: Boolean)


}
