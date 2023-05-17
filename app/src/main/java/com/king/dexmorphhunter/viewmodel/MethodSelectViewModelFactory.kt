package com.king.dexmorphhunter.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.king.dexmorphhunter.model.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class MethodSelectViewModelFactory @Inject constructor(private var context: Context, private var appRepository: AppRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MethodSelectViewModel::class.java)) {
            return MethodSelectViewModel(context,appRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
