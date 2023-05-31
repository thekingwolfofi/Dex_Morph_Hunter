package com.king.dexmorphhunter.model

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.king.dexmorphhunter.model.repository.AppRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class PackageRemovedReceiver @Inject constructor(
        private val appRepository: AppRepository
    ): BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {

        if (intent?.action == Intent.ACTION_PACKAGE_REMOVED) {
            runBlocking {
                appRepository.invalidateCache(context)
            }
        }
    }
}
