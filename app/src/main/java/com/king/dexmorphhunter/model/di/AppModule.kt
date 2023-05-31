package com.king.dexmorphhunter.model.di

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import androidx.room.Room
import com.king.dexmorphhunter.App
import com.king.dexmorphhunter.model.db.AppDatabase
import com.king.dexmorphhunter.model.db.AppInfoDao
import com.king.dexmorphhunter.model.db.AppSettingsDao
import com.king.dexmorphhunter.model.repository.AppRepository
import com.king.dexmorphhunter.model.util.PackageFinderUtils
import com.king.dexmorphhunter.xposed.MethodExtractorXposedModule
import com.king.dexmorphhunter.view.adapter.AppListAdapter
import com.king.dexmorphhunter.view.adapter.ArgumentsListAdapter
import com.king.dexmorphhunter.view.adapter.MethodListAdapter
import com.king.dexmorphhunter.viewmodel.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ActivityComponent::class)
object AppModule {

    @Provides
    fun provideContext(): Context {
        return App.instance.applicationContext
    }

    @Provides
    fun provideAppSettingsDao(appDatabase: AppDatabase): AppSettingsDao{
        return appDatabase.appSettingsDao()
    }

    @Provides
    fun provideAppInfoDao(appDatabase: AppDatabase): AppInfoDao {
        return appDatabase.appInfoDao()
    }


    @Provides
    fun provideAppListAdapter(
        appListViewModel: AppListViewModel
    ): AppListAdapter {
        val updateIsIntercepted: (String, Boolean) -> Unit = { packageName, isIntercepted ->
            appListViewModel.updateIsIntercepted(packageName, isIntercepted)
        }

        val getBitmapFromPackage: (String) -> Bitmap? = { packageName ->
            appListViewModel.getBitmapFromPackage(packageName)
        }

        return AppListAdapter(appListViewModel, updateIsIntercepted, getBitmapFromPackage)
    }

    @Provides
    fun provideMethodListAdapter(
        ): MethodListAdapter {
            return MethodListAdapter()
        }

    @Provides
    fun provideArgumentsListAdapter(
    ): ArgumentsListAdapter {
        return ArgumentsListAdapter()
    }

    @Provides
    fun provideMethodExtractorXposedModule(): MethodExtractorXposedModule {
        return MethodExtractorXposedModule()
    }

    @Provides
    fun provideClassesPackageUtils(): PackageFinderUtils {
        return PackageFinderUtils
    }

}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun provideAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "app_database")
            .build()
    }



    @Provides
    fun provideMethodSelectViewModel(
        appRepository: AppRepository
    ): MethodSelectViewModel {
        return MethodSelectViewModel(appRepository)
    }

    @Provides
    fun provideParameterEditorViewModel(
        appRepository: AppRepository
    ): ParameterEditorViewModel {
        return ParameterEditorViewModel(appRepository)
    }


    @Provides
    fun provideAppListViewModel(
        appRepository: AppRepository
    ): AppListViewModel {
        return AppListViewModel(appRepository)
    }


    @Provides
    fun provideAppRepository( appDatabase: AppDatabase): AppRepository {
        return AppRepository(appDatabase)
    }
}


