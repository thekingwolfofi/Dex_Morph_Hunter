package com.king.dexmorphhunter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class MethodSelectViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MethodSelectViewModel::class.java)) {
            return MethodSelectViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
