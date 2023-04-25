package com.king.dexmorphhunter.model.repository

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.preference.PreferenceManager
import androidx.lifecycle.ViewModel
import com.king.dexmorphhunter.model.db.AppInfo
import com.king.dexmorphhunter.model.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.*
import android.util.Base64
@Suppress("DEPRECATION")
class AppRepository : ViewModel() {

    @SuppressLint("QueryPermissionsNeeded")
    @Suppress("DEPRECATION")
    suspend fun loadInstalledAppList(context: Context): List<AppInfo> = withContext(Dispatchers.Main){
        val sharedPrefs = context.getSharedPreferences("app_cache", Context.MODE_PRIVATE)
        val mAppList: MutableList<AppInfo>
        val reloadCache = sharedPrefs.getBoolean("cachedApps", false)
        if (!reloadCache) {
            val pm: PackageManager by lazy { context.packageManager}
            val appList: MutableList<AppInfo> = mutableListOf()
            withContext(Dispatchers.IO) {
                val packages = pm.getInstalledPackages(0)
                for (packageInfo in packages) {
                    if(pm.getApplicationLabel(packageInfo.applicationInfo).toString() in Constants.removePackage){
                        continue
                    }
                    if(packageInfo.packageName in Constants.importantPackagesList){
                        continue
                    }
                    val appName = pm.getApplicationLabel(packageInfo.applicationInfo).toString()
                    val isSystem = isSystemApp(context,packageInfo.packageName)
                    appList.add(AppInfo(packageInfo.packageName, appName,null,isSystem,false))
                    // Salva o cache no SharedPreferences
                    cacheAppInfo(context, AppInfo(packageInfo.packageName,appName))
                }
            }

            mAppList = appList
            return@withContext mAppList
        }else {
            val cache = getCachedAppList(context)
            mAppList = cache
            return@withContext mAppList
        }

    }

    fun updateIsIntercepted(context: Context, packageName: String, isIntercepted: Boolean) {
        val sharedPrefs = context.getSharedPreferences("app_cache", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putBoolean("${packageName}_intercepted_app", isIntercepted)
        editor.apply()
    }

    suspend fun invalidateCache(context: Context) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.clear()
        editor.apply()

        loadInstalledAppList(context)
    }

    @Suppress("DEPRECATION")
    fun getBitmapFromPackage(context: Context, packageName: String): Drawable {
        val pm = context.packageManager
        val applicationInfo = pm.getApplicationInfo(packageName, 0)
        return pm.getApplicationIcon(applicationInfo)
    }

    @Suppress("DEPRECATION")
    fun isSystemApp(context: Context, packageName: String): Boolean {
        val pm = context.packageManager
        val applicationInfo = pm.getApplicationInfo(packageName, 0)
        return packageName in Constants.importantPackagesList || applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }

    private fun isInterceptedApp(context: Context, packageName: String): Boolean {
        val sharedPrefs = context.getSharedPreferences("app_cache", Context.MODE_PRIVATE)
        return sharedPrefs.getBoolean(packageName, false)
    }

    fun filterInterceptedApps(checked: Boolean, appList: List<AppInfo>): List<AppInfo> {
        return appList.filter {
            it.appIsIntercepted == checked
        }.sortedBy { it.appName }
    }

    fun filterSystemApps(context: Context, checked: Boolean, appList: List<AppInfo>): List<AppInfo> {
        return appList.filter { isSystemApp(context,it.packageName) == checked }.sortedBy { it.appName }
    }

    fun filterApps(
        query: String?,
        appList: List<AppInfo>?
    ): List<AppInfo>? {
        return appList?.filter {
            val appName = it.appName.lowercase(Locale.getDefault())
            val packageName = it.packageName.lowercase(Locale.getDefault())
            query?.let { q ->
                appName.contains(q.lowercase(Locale.getDefault())) ||
                        packageName.contains(q.lowercase(Locale.getDefault()))
            } ?: true
        }?.sortedBy { it.appName }
    }

    private fun cacheAppInfo(context: Context, appInfo: AppInfo) {
        val prefs = context.getSharedPreferences("app_cache", Context.MODE_PRIVATE)
        var editor = prefs.edit()
        editor.putString("${appInfo.packageName}_package", appInfo.packageName)
        editor.putString("${appInfo.packageName}_name", appInfo.appName)
        editor.putString("${appInfo.packageName}_icon", encodeBitmap(appInfo.appIcon))
        editor.putBoolean(
            "${appInfo.packageName}_system_app",
            appInfo.appIsSystemApp ?: isSystemApp(context,appInfo.packageName)
        )
        editor.putBoolean(
            "${appInfo.packageName}_intercepted_app",
            appInfo.appIsIntercepted ?: isInterceptedApp(context,appInfo.packageName)
        )
        editor.apply()
        val sharedPrefs = context.getSharedPreferences("app_cache", Context.MODE_PRIVATE)
        editor = sharedPrefs.edit()
        editor.putBoolean("cachedApps",true)
        editor.apply()
    }

    private fun getCachedAppList(context: Context): MutableList<AppInfo> {
        val appList = mutableListOf<AppInfo>()
        val prefs = context.getSharedPreferences("app_cache", Context.MODE_PRIVATE)
        prefs.getBoolean("interceptedAppsSwitch",false)
        prefs.getBoolean("systemAppsSwitch",false)
        val packages = prefs.all.keys.filter { it.endsWith("_package") }.map { it.removeSuffix("_package") }
        for (pkgName in packages) {
            val packageName = prefs.getString("${pkgName}_package",null)
            val name = prefs.getString("${pkgName}_name", null)
            val iconStr = prefs.getString("${pkgName}_icon", null)
            val isSystemApp = prefs.getBoolean("${pkgName}_system_app", false)
            val isIntercepted = prefs.getBoolean("${pkgName}_intercepted_app", false)
            val icon = decodeBitmap(iconStr)
            appList.add(AppInfo(packageName!!, name!!, icon, isSystemApp, isIntercepted))
        }
        return appList
    }

    /*
    fun getCachedAppInfo(context: Context, packageName: String): AppInfo? {
        val sharedPrefs = context.getSharedPreferences("app_cache", Context.MODE_PRIVATE)
        val cached = sharedPrefs.getBoolean("cachedApps", false)
        if (cached) {
            val prefs = context.getSharedPreferences("app_cache", Context.MODE_PRIVATE)
            val appName = prefs.getString("${packageName}_name", null)
            val iconString = prefs.getString("${packageName}_icon", null)
            val isSystemApp = prefs.getBoolean("${packageName}_system_app", false)
            val isInterceptedApp = prefs.getBoolean("${packageName}_intercepted_app", false)
            val icon = if (iconString != null) decodeBitmap(iconString) else null
            return if (appName != null) AppInfo(
                packageName,
                appName,
                icon,
                isSystemApp,
                isInterceptedApp
            ) else null
        }
        return null
    }
     */

    private fun encodeBitmap(bitmap: Bitmap?): String? {
        if (bitmap == null) {
            return null
        }
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun decodeBitmap(bitmapString: String?): Bitmap? {
        if (bitmapString == null) {
            return null
        }
        val byteArray = Base64.decode(bitmapString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

}

