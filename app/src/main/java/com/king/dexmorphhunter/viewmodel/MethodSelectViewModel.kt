package com.king.dexmorphhunter.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.king.dexmorphhunter.model.data.ClassInfo
import com.king.dexmorphhunter.model.data.MethodInfo
import com.king.dexmorphhunter.model.repository.AppRepository

@SuppressLint("StaticFieldLeak")
class MethodSelectViewModel( var context: Context, var appRepository: AppRepository ) : ViewModel() {

    // Aqui você pode definir variáveis para armazenar os dados da lista de classes e métodos.
    private val _classList = MutableLiveData<List<ClassInfo>>()
    val classList: LiveData<List<ClassInfo>>
        get() = _classList

    private val _methodList = MutableLiveData<List<MethodInfo>>()
    val methodList: LiveData<List<MethodInfo>>
        get() = _methodList

    private var isFiltred: Boolean = true


    suspend fun getMethodList(classInfo: ClassInfo) {
        val methodList = appRepository.getMethodList(classInfo)
        _methodList.postValue(methodList)
    }


    suspend fun getClassList(packageName: String) {
        val classList = appRepository.filterClassList(packageName)
        _classList.postValue(classList)
    }



    suspend fun setFilterClass(packageName: String) {
        appRepository.setFilterClass()
        getClassList(packageName)

    }


}

