package com.king.dexmorphhunter.model

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.king.dexmorphhunter.model.db.AppInfo
import com.king.dexmorphhunter.model.repository.AppRepository
import com.king.dexmorphhunter.model.util.PackageUtils

class AppListModel(private val context: Context) : ViewModel() {
    private val appRepository = AppRepository()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    suspend fun getInstalledAppList(): LiveData<List<AppInfo>> {
        return appRepository.getInstalledAppList(context)
    }


    fun updateIsIntercepted(packageName: String, isIntercepted: Boolean) {
        appRepository.updateIsIntercepted(context, packageName, isIntercepted )
    }

    fun invalidateCache() {
        appRepository.invalidateCache(context)
    }
    /*
    fun loadPackageInfo(packageName: String): PackageInfo = runBlocking {
        packageCache.first()[packageName]!!.packageInfo
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




    fun loadAppLabel(packageName: String): String = runBlocking {
        packageCache.first()[packageName]!!.appName
    }
    */

    fun isSystemApp( packageName: String):Boolean{
        return appRepository.isSystemApp(context,packageName)
    }


    // Método para buscar ícone do aplicativo
    fun getBitmapFromPackage(packageName: String): Drawable {
        return appRepository.getBitmapFromPackage(context, packageName)
    }

    fun extractMethodFromApp(packageName: String) {
        // Instancia a classe MethodInfoExtractModule e chama o método extractMethods
        val listClasses = PackageUtils.getClassesInPackage(context,packageName)


        // Faça o que precisar com a lista de nomes de método, por exemplo, imprimir no logcat
        Log.d("MethodNames", "lista de classes " + listClasses.size)
    }

    fun isInterceptedApp(packageName: String): Boolean {
        return appRepository.isInterceptedApp(context,packageName)
    }

    fun filterInterceptedApps(checked: Boolean, appList: List<AppInfo>?): List<AppInfo>? {
        return appList?.let { appRepository.filterInterceptedApps(checked, it) }
    }

    fun filterSystemApps(checked: Boolean, appList: List<AppInfo>?): List<AppInfo>? {
        return appList?.let { appRepository.filterSystemApps(context,checked, it) }
    }

    fun filterApps(
        query: String?,
        appList: List<AppInfo>?
    ): List<AppInfo>? {
        return appRepository.filterApps( query, appList)
    }

}
