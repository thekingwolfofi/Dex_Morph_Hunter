package com.king.dexmorphhunter.model.data

import androidx.room.*

@Entity(tableName = "app_info")
data class AppInfo(
    @PrimaryKey
    val packageName: String,
    val appName: String,
    @ColumnInfo(name = "is_system_app")
    val isSystemApp: Boolean? = null,
    @ColumnInfo(name = "is_intercepted_app")
    val isInterceptedApp: Boolean? = null,
    @ColumnInfo(name = "clazzes_intercepted")
    val clazzIntercepted: List<String>? = null
)
