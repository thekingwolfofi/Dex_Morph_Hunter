package com.king.dexmorphhunter.viewmodel

import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.king.dexmorphhunter.model.AppListModel
import com.king.dexmorphhunter.model.db.AppInfo

class AppListViewModel(private val appListModel: AppListModel) : ViewModel() {

    private val _appList = MutableLiveData<List<AppInfo>>()
    val appList: LiveData<List<AppInfo>> = _appList

    fun loadInstalledApps(interceptedAppsChecked: Boolean, systemAppsChecked: Boolean, query: String? = null) {
        val list = appListModel.getInstalledApps(interceptedAppsChecked, systemAppsChecked, query)
        _appList.value = list
    }

    fun filterApps(interceptedAppsChecked: Boolean, systemAppsChecked: Boolean, query: String?) {
        val list = appListModel.getInstalledApps(interceptedAppsChecked, systemAppsChecked, query)
        _appList.value = list
    }

    fun updateInterceptedApp(packageName: String, checked: Boolean) {
        appListModel.updateInterceptedApp(packageName, checked)
    }

    class Factory(private val appListModel: AppListModel) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AppListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AppListViewModel(appListModel) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}