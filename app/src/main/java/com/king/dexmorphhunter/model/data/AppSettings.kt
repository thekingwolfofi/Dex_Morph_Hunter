package com.king.dexmorphhunter.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_settings")
data class AppSettings(
    @PrimaryKey
    val id: Int = 1,
    @ColumnInfo(name = "intercepted_Apps_Switch")
    val interceptedAppsSwitch: Boolean,
    @ColumnInfo(name = "system_Apps_Switch")
    val systemAppsSwitch: Boolean
)
