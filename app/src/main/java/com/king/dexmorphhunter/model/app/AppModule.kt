package com.king.dexmorphhunter.model.app

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import com.king.dexmorphhunter.databinding.ActivityMainBinding
import com.king.dexmorphhunter.model.data.ArgumentInfo
import com.king.dexmorphhunter.model.db.AppDatabase
import com.king.dexmorphhunter.model.db.AppInfoDao
import com.king.dexmorphhunter.model.db.AppSettingsDao
import com.king.dexmorphhunter.model.repository.AppRepository
import com.king.dexmorphhunter.view.adapter.AppListAdapter
import com.king.dexmorphhunter.viewmodel.AppListViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object AppModule {

    @Provides
    fun provideAppListViewModel(
        context: Context,
        appRepository: AppRepository
    ): AppListViewModel {
        return AppListViewModel(context, appRepository)
    }

    @Provides
    fun provideAppRepository(
        context: Context,
        appSettingsDao: AppSettingsDao,
        appInfoDao:AppInfoDao
    ): AppRepository {

        return AppRepository(appSettingsDao, appInfoDao)
    }

    @Provides
    fun provideAppListAdapter(
        context: Context,
        appListViewModel: AppListViewModel,
        updateIsIntercepted: (packageName: String, isIntercepted: Boolean) -> Unit,
        getBitmapFromPackage: (packageName: String) -> Bitmap?
    ): AppListAdapter {
        return AppListAdapter(context, appListViewModel, updateIsIntercepted, getBitmapFromPackage)
    }

    @Provides
    fun provideContext(): Context {
        return App.instance.applicationContext
    }

    @Provides
    fun provideAppDatabase(context: Context): AppDatabase{
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideAppInfoDao(appDatabase: AppDatabase): AppInfoDao{
        return appDatabase.appInfoDao()
    }

    @Provides
    fun provideAppSettingsDao(appDatabase: AppDatabase): AppSettingsDao{
        return appDatabase.appSettingsDao()
    }


}
