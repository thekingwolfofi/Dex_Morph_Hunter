package com.king.dexmorphhunter.model

import android.app.Application
import androidx.room.Room
import com.king.dexmorphhunter.model.db.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    companion object {
        lateinit var instance: App
            private set
        lateinit var db: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "app_database"
        ).build()


    }
}
