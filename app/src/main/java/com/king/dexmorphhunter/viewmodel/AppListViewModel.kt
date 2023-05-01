package com.king.dexmorphhunter.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.*
import com.king.dexmorphhunter.model.db.AppInfo
import com.king.dexmorphhunter.model.repository.AppRepository
import com.king.dexmorphhunter.model.util.PackageUtils

@Suppress("KotlinConstantConditions")
@SuppressLint("StaticFieldLeak")
class AppListViewModel(private val context: Context) : ViewModel() {

    private val _appList = MutableLiveData<List<AppInfo>>()
    val appList: LiveData<List<AppInfo>> = _appList

    private var _filtredApps = MutableLiveData<List<AppInfo>>()

    private val appRepository = AppRepository()


    suspend fun getInstalledAppList() {
        _appList.postValue(appRepository.loadInstalledAppList(context))

    }

    suspend fun invalidateCache() {
        appRepository.invalidateCache(context)
    }


    // Método para buscar ícone do aplicativo
    fun getBitmapFromPackage(packageName: String): Drawable {
        return appRepository.getBitmapFromPackage(context, packageName)
    }


    suspend fun filterInterceptedApps(checked: Boolean) {
        _filtredApps.postValue( appRepository.loadInstalledAppList(context) )
        if (checked) {
            _filtredApps.value?.let { appRepository.filterInterceptedApps(checked, it) }
            _appList.postValue(_filtredApps.value)
        }else{
            _appList.postValue(_filtredApps.value)
        }
    }

    suspend fun filterSystemApps(checked: Boolean){
        _filtredApps.postValue(appRepository.loadInstalledAppList(context))
        if (checked) {
            _filtredApps.value?.let { appRepository.filterSystemApps(context, checked, it) }
            _appList.postValue(_filtredApps.value)
        }else{
            _appList.postValue(_filtredApps.value)
        }
    }

    suspend fun filterApps(
        query: String?
    ){
        _filtredApps.postValue(appRepository.loadInstalledAppList(context))
        val list = appRepository.filterApps(query, _filtredApps.value)
        _appList.postValue(list)
    }


    fun updateIsIntercepted(packageName: String, isIntercepted: Boolean) {
        appRepository.updateIsIntercepted(context, packageName, isIntercepted)
        //if(isIntercepted) {
        //    val list = getExtractedClassesFromApp(context,packageName)
        //}
    }

    @SuppressLint("CommitPrefEdits")
    fun getExtractedClassesFromApp(context: Context, packageName: String): List<String> {
        // Retorna os valores
        return PackageUtils.getListClassesInPackage(context, packageName)
    }

}