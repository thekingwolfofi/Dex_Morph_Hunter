package com.king.dexmorphhunter.model.repository

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.king.dexmorphhunter.model.db.AppInfo
import com.king.dexmorphhunter.model.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppRepository() : ViewModel() {

    private val packageCache = MutableStateFlow(mutableMapOf<String, AppInfo>())

    val mRefreshing = MutableSharedFlow<Boolean>(replay = 1)

    private val mAppList = MutableSharedFlow<MutableList<String>>(replay = 1)

    var reloadCache = false

    suspend fun getInstalledAppList(context: Context): LiveData<List<AppInfo>> = withContext(Dispatchers.Main){
        val sharedPrefs = context.getSharedPreferences("dexApplication", Context.MODE_PRIVATE)
        reloadCache = sharedPrefs.getBoolean("reloadCache", true)

        val mAppList = MutableLiveData<List<AppInfo>>()
        //invalidateCache(context)
        /*if (reloadCache) {*/
            val pm: PackageManager by lazy { context.packageManager }
            val editor = sharedPrefs.edit()

            val appList: MutableList<AppInfo> = mutableListOf()
            val cacheList: MutableList<String> = mutableListOf()
            mRefreshing.emit(true)
            withContext(Dispatchers.IO) {
                val packages = pm.getInstalledPackages(0)
                for (packageInfo in packages) {
                    val appName = pm.getApplicationLabel(packageInfo.applicationInfo).toString()
                    appList.add(AppInfo(packageInfo.packageName, appName))
                    cacheList.add(packageInfo.packageName)
                }
            }

            mRefreshing.emit(false)
            // Salva o cache no SharedPreferences
            editor.putStringSet("packageCache", cacheList.toSet())
            editor.putBoolean("reloadCache", false)
            editor.apply()
            //val appListCache =
            mAppList.postValue(appList)
            return@withContext mAppList
        /*}else {
            // Carrega a lista do cache
            //val appListCache = Gson().fromJson(sharedPrefs.getString("packageCache", ""), Array<AppInfo>::class.java).toList()
            val cache = getAppInfoFromCache(context)
            mAppList.postValue(cache)
            return@withContext mAppList
        }
         */

    }

    /*
    private fun getAppInfoFromCache(context: Context): List<AppInfo> {
        val pm: PackageManager by lazy { context.packageManager }
        val sharedPrefs = context.getSharedPreferences("dexApplication", Context.MODE_PRIVATE)

        val appList = mutableListOf<AppInfo>()
        sharedPrefs.getStringSet("packageCache", emptySet())?.let { cachedPackages ->
            cachedPackages.forEach { cachedPackageName ->
                val packageName = cachedPackageName.toString()
                val packageInfo = pm.getPackageInfo(packageName, 0)
                val appName = pm.getApplicationLabel(packageInfo.applicationInfo).toString()
                //val icon = pm.getApplicationIcon(packageInfo.applicationInfo)
                //val isSystemApp = packageName in Constants.importantPackagesList || packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
                //val isIntercepted = sharedPrefs.getBoolean(packageName, false)
                appList.add(AppInfo(packageName, appName))

            }
        }
        return appList
    }
     */

    fun updateIsIntercepted(context: Context, packageName: String, isIntercepted: Boolean) {
        val sharedPrefs = context.getSharedPreferences("dexApplication", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putBoolean(packageName, isIntercepted)
        editor.apply()
    }


    fun invalidateCache(context: Context) {
        val pm: PackageManager by lazy { context.packageManager }
        val sharedPrefs = context.getSharedPreferences("dexApplication", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()

        viewModelScope.launch {
            mRefreshing.emit(true)
            val cache = withContext(Dispatchers.IO) {
                val packages = pm.getInstalledPackages(0)
                mutableMapOf<String, AppInfo>().also {
                    for (packageInfo in packages) {
                        val label = pm.getApplicationLabel(packageInfo.applicationInfo).toString()
                        it[packageInfo.packageName] = AppInfo(packageInfo.packageName, label)
                    }
                }
            }
            packageCache.emit(cache) //essa linha
            mAppList.emit(cache.keys.toMutableList())
            mRefreshing.emit(false)
            editor.putStringSet("packageCache", cache.keys.toSet())
            editor.apply()
        }
    }

    fun getBitmapFromPackage(context: Context, packageName: String): Drawable {
        val pm = context.packageManager
        val applicationInfo = pm.getApplicationInfo(packageName, 0)
        return pm.getApplicationIcon(applicationInfo)
    }

    fun isSystemApp(context: Context, packageName: String): Boolean {
        val pm = context.packageManager
        val applicationInfo = pm.getApplicationInfo(packageName, 0)
        return packageName in Constants.importantPackagesList || applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }

    fun isInterceptedApp(context: Context, packageName: String): Boolean {
        val sharedPrefs = context.getSharedPreferences("dexApplication", Context.MODE_PRIVATE)
        return sharedPrefs.getBoolean(packageName, false)
    }

    fun filterInterceptedApps(checked: Boolean, appList: List<AppInfo>): List<AppInfo> {
        return appList?.filter { it.appIsIntercepted == checked } ?: emptyList()
    }

    fun filterSystemApps(context: Context ,checked: Boolean, appList: List<AppInfo>): List<AppInfo>? {
        return appList?.filter { isSystemApp(context,it.packageName) } ?: emptyList()
    }

    fun filterApps(
        query: String?,
        appList: List<AppInfo>?
    ): List<AppInfo>? {
        return if (query != null) {
            appList?.filter {
                (it.packageName in query ||
                        it.appName in query)
            }?:emptyList()
        } else{
            appList
        }
    }

}

