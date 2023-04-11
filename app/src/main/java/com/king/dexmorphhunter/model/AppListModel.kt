package com.king.dexmorphhunter.model

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import com.king.dexmorphhunter.model.db.AppInfo

class AppListModel(private val context: Context) : ViewModel() {
    private var appList: List<AppInfo> = emptyList()

    fun getInstalledApps(interceptedAppsChecked: Boolean, systemAppsChecked: Boolean, query: String?): List<AppInfo> {
        val pm = context.packageManager
        val packages = pm.getInstalledPackages(PackageManager.GET_META_DATA)
        val filteredList = mutableListOf<AppInfo>()

        for (pkg in packages) {
            val appName = pm.getApplicationLabel(pkg.applicationInfo).toString()
            val packageName = pkg.packageName
            val isSystemApp = pkg.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
            val appIcon = pm.getApplicationIcon(pkg.applicationInfo)
            val appInfo = AppInfo(packageName, appName, appIcon, isSystemApp, false)

            if (query != null && query.isNotEmpty()) {
                if (!appInfo.appName.contains(query, true) && !appInfo.packageName.contains(query, true)) {
                    continue
                }
            }

            if (interceptedAppsChecked && appInfo.isInterceptedApp) {
                continue
            }
            if (systemAppsChecked && !appInfo.isSystemApp){
                continue
            }

            filteredList.add(appInfo)

        }

        appList = filteredList
        return appList
    }


    fun updateInterceptedApp(packageName: String, checked: Boolean) {
        appList.find { it.packageName == packageName }?.isInterceptedApp = checked
    }

    fun filter(newText: String?) {
        TODO("Not yet implemented")
    }
}
