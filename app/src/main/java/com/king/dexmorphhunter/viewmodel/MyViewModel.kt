package com.king.dexmorphhunter.viewmodel

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.king.dexmorphhunter.model.AppInfo
import com.king.dexmorphhunter.model.App

class MyViewModel(application: Application) : AndroidViewModel(application) {

    private val interceptedAppsList = mutableListOf<String>()
    val installedAppsList = MutableLiveData<List<AppInfo>?>()


    private fun getInterceptedApps(): List<String> {

        return interceptedAppsList
    }

    fun updateAppList() {
        val interceptedApps = getInterceptedApps()
        val showSystemApps: Boolean = interceptedAppsSwitchChecked.value ?: false
        val showInterceptedApps: Boolean = systemAppsSwitchChecked.value ?: false
        val installedApps: List<AppInfo> = fetchInstalledApps(interceptedApps)
        var appList: List<AppInfo> = installedApps

        if (!showSystemApps) {
            appList = installedApps.filterNot { it.isSystemApp }
        }
        if (showInterceptedApps) {
            appList = appList.filter { it.isInterceptedApp }
        }

        installedAppsList.postValue(appList)
    }

    private fun fetchInstalledApps(interceptedApps: List<String>): List<AppInfo> {
        val packageManager = App.instance.packageManager
        val packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        val installedApps = mutableListOf<AppInfo>()

        for (applicationInfo in packages) {
            val packageName = applicationInfo.packageName
            val appName = packageManager.getApplicationLabel(applicationInfo).toString()
            val appIcon = packageManager.getApplicationIcon(applicationInfo)
            val isSystemApp = (applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
            val isInterceptedApp = interceptedApps.contains(packageName)
            val appInfo = AppInfo(packageName, appName, appIcon, isSystemApp, isInterceptedApp )
            installedApps.add(appInfo)
        }

        return installedApps
    }

    fun getInstalledAppsList(): MutableLiveData<List<AppInfo>?> {
        return installedAppsList
    }

    fun setInterceptedApp(packageName: String, isIntercepted: Boolean) {
        if (isIntercepted) {
            interceptedAppsList.add(packageName)
        } else {
            interceptedAppsList.remove(packageName)
        }
        updateAppList()
    }

    private var _interceptedAppsSwitchChecked = MutableLiveData(false)
    val interceptedAppsSwitchChecked: LiveData<Boolean>
        get() = _interceptedAppsSwitchChecked

    fun onInterceptedAppsSwitchCheckedChanged(checked: Boolean) {
        _interceptedAppsSwitchChecked.value = checked
        updateAppList()
    }

    private var _systemAppsSwitchChecked = MutableLiveData(false)
    val systemAppsSwitchChecked: LiveData<Boolean>
        get() = _systemAppsSwitchChecked

    fun onSystemAppsSwitchCheckedChanged(checked: Boolean) {
        _systemAppsSwitchChecked.value = checked
        updateAppList()
    }

}
