package com.king.dexmorphhunter.viewmodel

import android.graphics.drawable.Drawable
import androidx.lifecycle.*
import com.king.dexmorphhunter.model.AppListModel
import com.king.dexmorphhunter.model.db.AppInfo

class AppListViewModel(private val appListModel: AppListModel) : ViewModel() {

    private val _appList = MutableLiveData<List<AppInfo>>()
    val appList: LiveData<List<AppInfo>> = _appList

    suspend fun loadInstalledAppList() {
        _appList.postValue(appListModel.getInstalledAppList().value)
    }

    fun isSystemApp(packageName: String):Boolean{
        return appListModel.isSystemApp(packageName)
    }
    fun isInterceptedApp(packageName: String):Boolean{
        return appListModel.isInterceptedApp(packageName)
    }

    fun getBitmapFromPackage(packageName: String): Drawable {
        return appListModel.getBitmapFromPackage(packageName)
    }


    fun updateInterceptedApp(packageName: String, isIntercepted: Boolean) {
        appListModel.updateIsIntercepted(packageName, isIntercepted)
        //appListModel.extractMethodFromApp(packageName)
    }

    suspend fun filterInterceptedApps(checked: Boolean) {
        val list = appList.value?.let { appListModel.filterInterceptedApps(checked, it) }
        if (list != null) {
            if(list.size != _appList.value?.size ?: -1) {
                _appList.postValue(list)
            } else{
                loadInstalledAppList()
            }
        }
    }

    suspend fun filterSystemApps(checked: Boolean) {
        val list = appList.value?.let { appListModel.filterSystemApps(checked, it) }
        if (list != null) {
            if(list.size != _appList.value?.size ?: -1) {
                _appList.postValue(list)
            } else{
                loadInstalledAppList()
            }
        }
    }


    suspend fun filterApps(query: String?) {
        val list = appListModel.filterApps( query,appList.value)

        if (list != null) {
            if(list.size == _appList.value?.size ?: 0) {
                _appList.postValue(list)
            } else{
                loadInstalledAppList()
            }
        }
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