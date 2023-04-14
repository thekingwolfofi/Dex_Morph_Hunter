package com.king.dexmorphhunter.viewmodel

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.lifecycle.*
import com.king.dexmorphhunter.model.AppListModel
import com.king.dexmorphhunter.model.db.AppInfo
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class AppListViewModel(private val appListModel: AppListModel) : ViewModel() {

    private val _appList = MutableLiveData<List<AppInfo>>()
    val appList: LiveData<List<AppInfo>> = _appList

    suspend fun loadInstalledAppList() {
            _appList.postValue(appListModel.getInstalledAppList().value)
    }

    fun filterApps(interceptedAppsChecked: Boolean, systemAppsChecked: Boolean, query: String?) {
        //val list = appListModel.getInstalledApps(interceptedAppsChecked, systemAppsChecked, query)
        //_appList.value = list
    }

    fun getBitmapFromPackage(packageName: String): Drawable {
        return appListModel.getBitmapFromPackage(packageName)
    }


    fun updateInterceptedApp(packageName: String, isIntercepted: Boolean) {
        appListModel.updateIsIntercepted(packageName, isIntercepted)
        //appListModel.extractMethodFromApp(packageName)
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