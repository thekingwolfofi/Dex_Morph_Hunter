@file:Suppress("DEPRECATION")

package com.king.dexmorphhunter.model.repository

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.preference.PreferenceManager
import android.util.Base64
import androidx.lifecycle.ViewModel
import com.king.dexmorphhunter.model.data.AppInfo
import com.king.dexmorphhunter.model.data.AppSettings
import com.king.dexmorphhunter.model.db.AppDatabase
import com.king.dexmorphhunter.model.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.*


@Suppress("DEPRECATION", "NAME_SHADOWING")
@SuppressLint("StaticFieldLeak")
class AppRepository( val context: Context) : ViewModel() {

    private val appDatabase = AppDatabase.getDatabase(context)
    private val appSettingsDao = appDatabase.appSettingsDao()
    private val appInfoDao = appDatabase.appInfoDao()


    @SuppressLint("QueryPermissionsNeeded")
    suspend fun loadInstalledAppList(context: Context): List<AppInfo> = withContext(Dispatchers.Main){
        val mAppList: List<AppInfo>
        val settingsIsNull = getSettings() == null
        if (settingsIsNull){
            withContext(Dispatchers.IO) {
                appSettingsDao.insertOrUpdateAppSettings(
                    AppSettings(
                        1,
                        interceptedAppsSwitch = false,
                        systemAppsSwitch = false
                    )
                )
            }
        }
        val reloadCache = appInfoDao.getAll().isEmpty()
        if (reloadCache) {
            val pm: PackageManager by lazy { context.packageManager}
            val appList: MutableList<AppInfo> = mutableListOf()
            withContext(Dispatchers.IO) {
                val packages = pm.getInstalledPackages(0)
                for (packageInfo in packages) {
                    if (pm.getApplicationLabel(packageInfo.applicationInfo)
                            .toString() in Constants.removePackage
                    ) {
                        continue
                    }
                    if (packageInfo.packageName in Constants.importantPackagesList) {
                        continue
                    }
                    val appName = pm.getApplicationLabel(packageInfo.applicationInfo).toString()
                    val isSystem = isSystemApp(context, packageInfo.packageName)
                    val isIntercepted =
                        appInfoDao.getByPackageName(packageInfo.packageName)?.isInterceptedApp
                            ?: false
                    appList.add(AppInfo(packageInfo.packageName, appName, isSystem, isIntercepted))
                    // Salva o cache no Room
                    appInfoDao.insert(
                        AppInfo(
                            packageInfo.packageName,
                            appName,
                            isSystem,
                            isIntercepted
                        )
                    )
                }


                val isIntercepted = appSettingsDao.getAppSettings()?.interceptedAppsSwitch ?: false
                val isSystem = appSettingsDao.getAppSettings()?.systemAppsSwitch ?: false
                mAppList = filterApps("", isIntercepted, isSystem)
            }
            return@withContext mAppList
        } else {
            withContext(Dispatchers.IO) {
                val isIntercepted = appSettingsDao.getAppSettings()?.interceptedAppsSwitch ?: false
                val isSystem = appSettingsDao.getAppSettings()?.systemAppsSwitch ?: false
                mAppList = filterApps("", isIntercepted, isSystem)
            }
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

    private fun isSystemApp(context: Context, packageName: String): Boolean {
        val pm = context.packageManager
        val applicationInfo = pm.getApplicationInfo(packageName, 0)
        return packageName in Constants.importantPackagesList || applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }

    suspend fun filterApps(
        query: String?,
        isIntercepted: Boolean = false,
        isSystem: Boolean = false
    ): List<AppInfo> {
        val query = query ?: ""
        val isIntercepted = isIntercepted
        val isSystem = isSystem
        return withContext(Dispatchers.IO) {
            appInfoDao.testQuery(query = query, isSystem = isSystem, isIntercepted = isIntercepted)
        }
    }

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

    fun updateListApps(
        filterSystemApps: Boolean,
        filterInterceptedApps: Boolean
    ) : List<AppInfo>{

        appSettingsDao.insertOrUpdateAppSettings(AppSettings(
            1,
            filterInterceptedApps,
            filterSystemApps
        ))
        return emptyList()
    }

    suspend fun insertAll(appInfoList: List<AppInfo>) {
        withContext(Dispatchers.IO) {
            appInfoDao.insertAll(appInfoList)
        }
    }

    suspend fun getAll(): List<AppInfo> {
        return withContext(Dispatchers.IO) {
            appInfoDao.getAll()
        }
    }

    suspend fun getByPackageName(packageName: String): AppInfo? {
        return withContext(Dispatchers.IO) {
            appInfoDao.getByPackageName(packageName)
        }
    }

    private suspend fun getSettings(): AppSettings? {
        return withContext(Dispatchers.IO) {
            appSettingsDao.getAppSettings()
        }
    }

    suspend fun updateSettings(appSettings: AppSettings) {
        withContext(Dispatchers.IO) {
            appSettingsDao.insertOrUpdateAppSettings(appSettings)
        }
    }
}

