package com.king.dexmorphhunter.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.king.dexmorphhunter.model.repository.AppRepository

@Suppress("UNCHECKED_CAST")
class AppListViewModelFactory(private val context: Context, private val appRepository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppListViewModel::class.java)) {
            return AppListViewModel(context, appRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

