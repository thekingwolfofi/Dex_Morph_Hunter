package com.king.dexmorphhunter.model.util

object Constants {
    val removePackage = mutableSetOf(
        "res/",
        "dexmorphhunter"
    )

    val importantPackagesList = mutableSetOf(
        "android",
        "com.android",
        "android.media",
        "android.uid.system",
        "android.uid.shell",
        "android.uid.systemui",
        "com.android.permissioncontroller",
        "com.android.providers.downloads",
        "com.android.providers.downloads.ui",
        "com.android.providers.media",
        "com.android.providers.media.module",
        "com.android.providers.settings",
        "com.google.android.webview",
        "com.google.android.providers.media.module"
    )
}