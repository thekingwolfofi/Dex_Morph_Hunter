package com.king.dexmorphhunter.model

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.king.dexmorphhunter.model.db.AppInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppListModel(private val context: Context) : ViewModel() {
    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("intercepted_apps", Context.MODE_PRIVATE)
    }
    private val pm: PackageManager by lazy { context.packageManager }

    private var appList: List<AppInfo> = emptyList()
    private var isFrist = false

    fun getInstalledApps(interceptedAppsChecked: Boolean, systemAppsChecked: Boolean, query: String?): List<AppInfo> {
        if (appList.isNotEmpty()) {
            return filterApps(appList, interceptedAppsChecked, systemAppsChecked, query)
        }

        val packages = pm.getInstalledPackages(PackageManager.GET_META_DATA)
        val filteredList = mutableListOf<AppInfo>()

        for (pkg in packages) {
            val packageName = pkg.packageName
            val appName = pm.getApplicationLabel(pkg.applicationInfo).toString()
            val isInterceptedApp = getInterceptedAppFromCache(packageName)
            val isSystemApp = pkg.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
            val appIcon = pm.getApplicationIcon(pkg.applicationInfo)

            filteredList.add(AppInfo(packageName, appName, appIcon, isSystemApp, isInterceptedApp))
        }

        appList = filterApps(filteredList, interceptedAppsChecked, systemAppsChecked, query)
        return appList
    }

    fun updateInterceptedApp(packageName: String, checked: Boolean) {
        appList.find { it.packageName == packageName }?.isInterceptedApp = checked
        saveInterceptedAppToCache(packageName, checked)
    }

    private fun getInterceptedAppFromCache(packageName: String): Boolean {
        return sharedPreferences.getBoolean(packageName, false)
    }

    private fun saveInterceptedAppToCache(packageName: String, isIntercepted: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            sharedPreferences.edit().putBoolean(packageName, isIntercepted).apply()
        }
    }

    private fun filterApps(appList: List<AppInfo>,
                           interceptedAppsChecked: Boolean,
                           systemAppsChecked: Boolean,
                           query: String?
        ): List<AppInfo> {

        val resultList = mutableListOf<AppInfo>()
        for (app in appList) {

            if (interceptedAppsChecked && !app.isInterceptedApp) {
                continue
            }
            if (!systemAppsChecked && !app.isSystemApp) {
                continue
            }

            if (query != null && query.isNotEmpty()) {
                if (!app.appName.contains(query, true) &&
                    !app.packageName.contains(query, true)
                ) {
                    continue
                }
            }

            resultList.add(app)

        }
        return resultList
    }

}