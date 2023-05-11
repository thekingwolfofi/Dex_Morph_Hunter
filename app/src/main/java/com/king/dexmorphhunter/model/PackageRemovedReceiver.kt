package com.king.dexmorphhunter.model

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.king.dexmorphhunter.model.db.AppInfoDao
import com.king.dexmorphhunter.model.db.AppSettingsDao
import com.king.dexmorphhunter.model.repository.AppRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class PackageRemovedReceiver @Inject constructor(
        private val appSettingsDao: AppSettingsDao,
        private val appInfoDao: AppInfoDao
    ): BroadcastReceiver() {

    @SuppressLint("CommitPrefEdits")
    override fun onReceive(context: Context, intent: Intent?) {
        val appRepository = AppRepository(appSettingsDao, appInfoDao)
        if (intent?.action == Intent.ACTION_PACKAGE_REMOVED) {
            val sharedPrefs = context.getSharedPreferences("app_cache", Context.MODE_PRIVATE)
            val editor = sharedPrefs.edit()
            editor.putBoolean("cachedApps",false)
            runBlocking {
                appRepository.invalidateCache(context)
            }
        }
    }
}
