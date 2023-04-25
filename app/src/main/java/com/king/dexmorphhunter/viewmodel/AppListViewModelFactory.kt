package com.king.dexmorphhunter.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.king.dexmorphhunter.model.AppListModel

class AppListViewModelFactory(private val appListModel: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppListViewModel::class.java)) {
            return AppListViewModel(appListModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

