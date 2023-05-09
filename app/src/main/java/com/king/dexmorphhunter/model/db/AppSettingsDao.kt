package com.king.dexmorphhunter.model.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.king.dexmorphhunter.model.data.AppSettings

@Dao
interface AppSettingsDao {
    @Query("SELECT * FROM app_settings LIMIT 1")
    fun getAppSettings(): AppSettings?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateAppSettings(appSettings: AppSettings)

    /*
    @Query("UPDATE app_settings SET interceptedAppsSwitch = :isIntercepted WHERE id = 1")
    fun updateInterceptedAppsSwitch(isIntercepted: Boolean)

    @Query("UPDATE app_settings SET systemAppsSwitch = :isSystemApp WHERE id = 1")
    fun updateSystemAppsSwitch(isSystemApp: Boolean)
     */

}
