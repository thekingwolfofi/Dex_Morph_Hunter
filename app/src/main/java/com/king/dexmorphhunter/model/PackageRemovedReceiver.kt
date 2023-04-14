package com.king.dexmorphhunter.model

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.king.dexmorphhunter.model.repository.AppRepository

class PackageRemovedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val appRepository = AppRepository()
        if (intent?.action == Intent.ACTION_PACKAGE_REMOVED) {
            val sharedPrefs = context.getSharedPreferences("app_cache", Context.MODE_PRIVATE)
            val editor = sharedPrefs.edit()
            editor.putBoolean("cachedApps",false)
            appRepository.invalidateCache(context)
        }
    }
}
