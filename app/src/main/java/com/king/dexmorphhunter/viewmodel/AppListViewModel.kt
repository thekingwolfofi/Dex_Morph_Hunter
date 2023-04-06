package com.king.dexmorphhunter.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.king.dexmorphhunter.model.AppListModel
import com.king.dexmorphhunter.model.db.AppInfo

class AppListViewModel(private val appListModel: AppListModel) : ViewModel() {

    private val _appList = MutableLiveData<List<AppInfo>>()
    val appList: LiveData<List<AppInfo>> = _appList

    private var interceptedAppsSwitchChecked = false
    private var systemAppsSwitchChecked = false

    fun loadAppList(context: Context) {
        val appList = appListModel.getAppList(context)
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

    fun onInterceptedAppsSwitchCheckedChanged(isChecked: Boolean) {
        interceptedAppsSwitchChecked = isChecked
        filterAppList()
    }

    fun onSystemAppsSwitchCheckedChanged(isChecked: Boolean) {
        systemAppsSwitchChecked = isChecked
        filterAppList()
    }

    private fun filterAppList() {
        val appList = _appList.value.orEmpty().toMutableList()

        // Filtre a lista de aplicativos de acordo com as configurações dos switches
        val filteredList = appList.filter { app ->
            (!app.isSystemApp || systemAppsSwitchChecked) && (!app.isInterceptedApp || interceptedAppsSwitchChecked)
        }

        _appList.value = filteredList
    }
}
