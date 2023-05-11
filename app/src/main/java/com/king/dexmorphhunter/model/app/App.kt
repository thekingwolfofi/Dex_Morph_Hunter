package com.king.dexmorphhunter.model.app

import android.app.Application
import androidx.room.Room
import com.king.dexmorphhunter.model.db.AppDatabase
import dagger.hilt.android.HiltAndroidApp


class App : Application() {
    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this


    }
}

