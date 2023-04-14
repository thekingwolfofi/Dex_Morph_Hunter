package com.king.dexmorphhunter.model.db

import android.graphics.drawable.Drawable

data class AppInfo(
    val packageName: String,
    val appName: String,
    val appIcon: Drawable? = null,
    val appIsSystemApp: Boolean? = null,
    val appIsIntercepted: Boolean? = null
){

}


