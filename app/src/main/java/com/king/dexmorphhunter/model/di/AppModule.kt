package com.king.dexmorphhunter.model.di

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.king.dexmorphhunter.App
import com.king.dexmorphhunter.model.data.ArgumentInfo
import com.king.dexmorphhunter.model.db.AppDatabase
import com.king.dexmorphhunter.model.db.AppInfoDao
import com.king.dexmorphhunter.model.db.AppSettingsDao
import com.king.dexmorphhunter.model.repository.AppRepository
import com.king.dexmorphhunter.model.util.PackageFinderUtils
import com.king.dexmorphhunter.xposed.MethodExtractorXposedModule
import com.king.dexmorphhunter.view.adapter.AppListAdapter
import com.king.dexmorphhunter.view.adapter.ArgumentsListAdapter
import com.king.dexmorphhunter.view.adapter.MethodListAdapter
import com.king.dexmorphhunter.viewmodel.AppListViewModel
import com.king.dexmorphhunter.viewmodel.AppListViewModelFactory
import com.king.dexmorphhunter.viewmodel.MethodSelectViewModel
import com.king.dexmorphhunter.viewmodel.MethodSelectViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext

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
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java, "app-database"
        ).build()
    }

    @Provides
    fun provideAppListViewModel(
        context: Context,
        appRepository: AppRepository
    ): AppListViewModel {
        return AppListViewModel(context, appRepository)
    }

    @Provides
    fun provideAppListViewModelFactory(context: Context, appRepository: AppRepository): ViewModelProvider.Factory {
        return AppListViewModelFactory(context, appRepository)
    }


    @Provides
    fun provideAppRepository(context: Context, appDatabase: AppDatabase): AppRepository {

        return AppRepository(context ,appDatabase)
    }

    /*
    @Provides
    fun providePackageRemovedReceiver(
        appSettingsDao: AppSettingsDao,
        appInfoDao: AppInfoDao
    ) : PackageRemovedReceiver {
        return PackageRemovedReceiver(appSettingsDao, appInfoDao)
    }
     */

    @Provides
    fun provideAppListAdapter(
        context: Context,
        appListViewModel: AppListViewModel
    ): AppListAdapter {
        val updateIsIntercepted: (String, Boolean) -> Unit = { packageName, isIntercepted ->
            appListViewModel.updateIsIntercepted(packageName, isIntercepted)
        }

        val getBitmapFromPackage: (String) -> Bitmap? = { packageName ->
            appListViewModel.getBitmapFromPackage(packageName)
        }

        return AppListAdapter(context, appListViewModel, updateIsIntercepted, getBitmapFromPackage)
    }

    @Provides
    fun provideMethodListAdapter(
            context: Context,
            methodSelectViewModel: MethodSelectViewModel
        ): MethodListAdapter {
            return MethodListAdapter()
        }

    @Provides
    fun provideArgumentsListAdapter(
        argumentList: List<ArgumentInfo>
    ): ArgumentsListAdapter {
        return ArgumentsListAdapter(argumentList)
    }

    @Provides
    fun provideMethodExtractorXposedModule(): MethodExtractorXposedModule {
        return MethodExtractorXposedModule()
    }

    @Provides
    fun provideClassesPackageUtils(): PackageFinderUtils {
        return PackageFinderUtils
    }

    @Provides
    fun provideMethodSelectViewModel(
        context: Context,
        appRepository: AppRepository
    ): MethodSelectViewModel {
        return MethodSelectViewModel(context, appRepository)
    }
    @Provides
    fun provideMethodSelectViewModelFactory(context: Context, appRepository: AppRepository): MethodSelectViewModelFactory {
        return MethodSelectViewModelFactory(context, appRepository)
    }
}

