package com.king.dexmorphhunter.model.db

import android.graphics.Bitmap

data class AppInfo(
    val packageName: String,
    val appName: String,
    val appIcon: Bitmap? = null,
    val appIsSystemApp: Boolean? = null,
    val appIsIntercepted: Boolean? = null
)

