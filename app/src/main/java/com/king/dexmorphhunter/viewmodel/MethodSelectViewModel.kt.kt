package com.king.dexmorphhunter.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.king.dexmorphhunter.model.MethodInfo
import com.king.dexmorphhunter.repository.MethodSelectRepository

class MethodSelectViewModel : ViewModel() {
    private val repository = MethodSelectRepository()

    val methodInfoList = MutableLiveData<List<MethodInfo>>()

    fun extractMethodsAndClasses(packageName: String) {
        methodInfoList.postValue(repository.extractMethodsAndClasses(packageName))
    }

    fun startInterceptService(className: String, methodName: String) {
        repository.startInterceptService(className, methodName)
    }
}
