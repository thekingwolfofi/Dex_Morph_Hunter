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

/*
    private val appRepository = AppRepository()

    suspend fun getInstalledAppList(): List<AppInfo> {
        val list = appRepository.getInstalledAppList(context).value ?: emptyList()
        return list
    }


    fun updateIsIntercepted(packageName: String, isIntercepted: Boolean) {
        appRepository.updateIsIntercepted(context, packageName, isIntercepted )
    }

    fun invalidateCache() {
        appRepository.invalidateCache(context)
    }

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

    suspend fun filterInterceptedApps(checked: Boolean): List<AppInfo>? {
        val appList = appRepository.getInstalledAppList(context).value ?: emptyList()
        return appRepository.filterInterceptedApps(checked, appList)
    }

    suspend fun filterSystemApps(checked: Boolean): List<AppInfo> {
        val appList = appRepository.getInstalledAppList(context).value ?: emptyList()
        return appRepository.filterSystemApps(context, checked, appList)
    }

    suspend fun filterApps(
        query: String?
    ): List<AppInfo>? {
        val appList = appRepository.getInstalledAppList(context)
        return appRepository.filterApps( query, appList.value)
    }

 */
}
