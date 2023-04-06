package com.king.dexmorphhunter.viewmodel

import android.content.Intent
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.king.dexmorphhunter.model.AppInfo

class AppListViewModel : ViewModel() {

    private val _appList = MutableLiveData<List<AppInfo>>()
    val appList: LiveData<List<AppInfo>> = _appList

    fun loadAppList(context: Context) {
        val pm: PackageManager = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val apps: List<ResolveInfo> = pm.queryIntentActivities(intent, 0)
        val appList = mutableListOf<AppInfo>()
        for (resolveInfo in apps) {
            val packageName = resolveInfo.activityInfo.packageName
            val appName = resolveInfo.loadLabel(pm).toString()
            val appIcon = resolveInfo.loadIcon(pm)
            val isSystemApp = (resolveInfo.activityInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
            val isInterceptedApp = false
            appList.add(AppInfo(packageName, appName, appIcon, isSystemApp, isInterceptedApp))
        }
        _appList.value = appList
    }

    fun updateAppInterceptionStatus(app: AppInfo, isIntercepted: Boolean) {
        val appList = _appList.value.orEmpty().toMutableList()
        val index = appList.indexOfFirst { it.packageName == app.packageName }
        if (index != -1) {
            val updatedApp = appList[index].copy(isInterceptedApp = isIntercepted)
            appList[index] = updatedApp
            _appList.value = appList
        }
    }
}