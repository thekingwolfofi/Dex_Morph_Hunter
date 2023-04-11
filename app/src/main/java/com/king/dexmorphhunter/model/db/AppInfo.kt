package com.king.dexmorphhunter.model.db

import android.graphics.drawable.Drawable

data class AppInfo(
    val packageName: String,
    val appName: String,
    var appIcon: Drawable? = null,
    val isSystemApp: Boolean = false,
    var isInterceptedApp: Boolean = false
){
    fun isIntercepted(): Boolean {
        return isInterceptedApp
    }

    fun isSystem(): Boolean {
        return isSystemApp
    }
}

