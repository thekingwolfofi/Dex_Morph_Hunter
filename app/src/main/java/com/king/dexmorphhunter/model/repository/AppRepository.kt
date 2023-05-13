@file:Suppress("DEPRECATION")

package com.king.dexmorphhunter.model.repository

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.Base64
import androidx.lifecycle.ViewModel
import com.king.dexmorphhunter.model.data.AppInfo
import com.king.dexmorphhunter.model.data.AppSettings
import com.king.dexmorphhunter.model.db.AppDatabase
import com.king.dexmorphhunter.model.db.AppInfoDao
import com.king.dexmorphhunter.model.db.AppSettingsDao
import com.king.dexmorphhunter.model.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject

@Suppress("DEPRECATION")
@HiltViewModel
class AppRepository @Inject constructor(
    appDatabase: AppDatabase
) : ViewModel() {

    private var appSettingsDao: AppSettingsDao = appDatabase.appSettingsDao()
    private var appInfoDao: AppInfoDao = appDatabase.appInfoDao()

    @SuppressLint("QueryPermissionsNeeded")
    suspend fun loadInstalledAppList(context: Context): List<AppInfo> = withContext(Dispatchers.Main){
        val mAppList: List<AppInfo>
        setupConfig()
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
                    insertAll(appList)
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

    private suspend fun setupConfig(){
        val settingsIsNull = getSettings() == null
        if (settingsIsNull) {
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
    }

    suspend fun updateIsIntercepted(packageName: String, isIntercepted: Boolean) {
        // Atualiza o valor de `isInterceptedApp` no banco de dados
        appInfoDao.updateIsIntercepted(packageName, isIntercepted)
    }

    suspend fun invalidateCache(context: Context) {
        // Exclui todas as entradas do banco de dados
        appInfoDao.deleteAll()

        // Carrega a lista de aplicativos instalados
        loadInstalledAppList(context)
    }

    fun getBitmapFromPackage(context: Context, packageName: String): Bitmap? {
        return try {
            val packageManager = context.packageManager
            val appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            val appIcon = appInfo.loadIcon(packageManager)
            val appBitmap = Bitmap.createBitmap(appIcon.intrinsicWidth, appIcon.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(appBitmap)
            appIcon.setBounds(0, 0, canvas.width, canvas.height)
            appIcon.draw(canvas)
            appBitmap
        } catch (e: Exception) {
            null
        }
    }


    private fun isSystemApp(context: Context, packageName: String): Boolean {
        val pm = context.packageManager
        val applicationInfo = pm.getApplicationInfo(packageName, 0)
        return packageName in Constants.importantPackagesList || applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }

    suspend fun filterApps(
        query: String? = "" ,
        isIntercepted: Boolean = false,
        isSystem: Boolean = false
    ): List<AppInfo> {
        return withContext(Dispatchers.IO) {
            appInfoDao.getFilterApps(query!!, isSystem, isIntercepted)
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

    private suspend fun insertAll(appInfoList: List<AppInfo>) {
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
