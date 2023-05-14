package com.king.dexmorphhunter.model.di

import android.content.Context
import androidx.room.Room
import com.king.dexmorphhunter.App
import com.king.dexmorphhunter.model.db.AppDatabase
import com.king.dexmorphhunter.model.db.AppInfoDao
import com.king.dexmorphhunter.model.db.AppSettingsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @Provides
    fun provideContext(): Context {
        return App.instance.applicationContext
    }

    @Provides
    fun provideAppSettingsDao(appDatabase: AppDatabase): AppSettingsDao {
        return appDatabase.appSettingsDao()
    }

    @Provides
    fun provideAppInfoDao(appDatabase: AppDatabase): AppInfoDao {
        return appDatabase.appInfoDao()
    }

    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java, "app-database"
        ).build()
    }
}