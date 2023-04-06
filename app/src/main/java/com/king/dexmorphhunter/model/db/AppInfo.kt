package com.king.dexmorphhunter.model.db

import android.graphics.drawable.Drawable

data class AppInfo(
    val packageName: String,
    val appName: String,
    val appIcon: Drawable?,
    val isSystemApp: Boolean,
    val isInterceptedApp: Boolean
)

