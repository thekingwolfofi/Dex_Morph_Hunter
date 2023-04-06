package com.king.dexmorphhunter.model
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import com.king.dexmorphhunter.model.db.AppInfo

class AppListModel(private val context: Context) {

    fun getAppList(context: Context): List<AppInfo> {
        val pm: PackageManager = this.context.packageManager
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
        return appList
    }
}