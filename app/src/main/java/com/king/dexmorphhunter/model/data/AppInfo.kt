package com.king.dexmorphhunter.model.data

import androidx.room.*

@Entity(tableName = "app_info")
data class AppInfo(
    @PrimaryKey
    @ColumnInfo(name = "package_name")
    val packageName: String,
    @ColumnInfo(name = "app_Name")
    val appName: String,
    @ColumnInfo(name = "is_system_app")
    val isSystemApp: Boolean? = null,
    @ColumnInfo(name = "is_intercepted_app")
    val isInterceptedApp: Boolean? = null
)
