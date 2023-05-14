package com.king.dexmorphhunter.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.*
import com.king.dexmorphhunter.model.data.AppInfo
import com.king.dexmorphhunter.model.data.AppSettings
import com.king.dexmorphhunter.model.db.AppDatabase
import com.king.dexmorphhunter.model.repository.AppRepository
import com.king.dexmorphhunter.model.util.ClassesPackageUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("KotlinConstantConditions")
@SuppressLint("StaticFieldLeak")
class AppListViewModel(
        val context: Context,
        private val appRepository: AppRepository
    ) : ViewModel() {


    private val _filterSystemApps = MutableLiveData(false)
    val filterSystemApps: LiveData<Boolean> = _filterSystemApps

    private val _filterInterceptedApps = MutableLiveData(false)
    val filterInterceptedApps: LiveData<Boolean> = _filterInterceptedApps

    private val _filterQueryApps = MutableLiveData("")
    val filterQueryApps: LiveData<String> = _filterQueryApps

    private val _appList = MutableLiveData<List<AppInfo>>()
    val appList: LiveData<List<AppInfo>> = _appList

    suspend fun getInstalledAppList() {
        _appList.postValue(appRepository.loadInstalledAppList(context))
    }

    suspend fun invalidateCache() {
        appRepository.invalidateCache(context)
    }

    // Método para buscar ícone do aplicativo
    fun getBitmapFromPackage(packageName: String): Bitmap? {
        return appRepository.getBitmapFromPackage(context, packageName)
    }

    fun filterInterceptedApps(checked: Boolean) {
        _filterInterceptedApps.postValue(checked)
    }

    fun filterSystemApps(checked: Boolean){
        _filterSystemApps.postValue(checked)
    }

    fun filterQueryApps(query: String?){
        viewModelScope.launch {
            _filterQueryApps.postValue(query)
        }
    }

    fun updateIsIntercepted(packageName: String, isIntercepted: Boolean) {
        viewModelScope.launch {
            appRepository.updateIsIntercepted(packageName, isIntercepted)
        }
    }

    fun updateListApps() {
        viewModelScope.launch {
            val query = filterQueryApps.value ?: ""
            val interceptedApps = filterInterceptedApps.value ?: false
            val systemApps = filterSystemApps.value ?: false

            val appDatabase = AppDatabase.getDatabase(context)
            if (appDatabase != null) {
                val settings = withContext(Dispatchers.IO) {
                    val appSettingsDao = AppDatabase.getDatabase(context).appSettingsDao()
                    appSettingsDao.getAppSettings()
                }

                if (settings != null) {
                    if (settings.systemAppsSwitch != systemApps || settings.interceptedAppsSwitch != interceptedApps) {
                        val appSettings = AppSettings(1, interceptedApps, systemApps)
                        appRepository.updateSettings(appSettings)
                    }
                }

                val appList = withContext(Dispatchers.IO) {
                    appRepository.filterApps(query, interceptedApps, systemApps)
                }
                _appList.postValue(appList)
            }
        }
    }

    fun getExtractedClassesFromApp(context: Context, packageName: String): List<String> {
        return ClassesPackageUtils.getListClassesInPackage(context, packageName)
    }

}
