package com.king.dexmorphhunter.model

import android.graphics.drawable.Drawable

data class AppInfo(
    val packageName: String,
    val name: String,
    val icon: Drawable,
    val isSystemApp: Boolean,
    val isInterceptedApp: Boolean
) {

}
