package com.king.dexmorphhunter.model.app

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.king.dexmorphhunter.model.PackageRemovedReceiver
import com.king.dexmorphhunter.model.data.ArgumentInfo
import com.king.dexmorphhunter.model.db.AppDatabase
import com.king.dexmorphhunter.model.db.AppInfoDao
import com.king.dexmorphhunter.model.db.AppSettingsDao
import com.king.dexmorphhunter.model.repository.AppRepository
import com.king.dexmorphhunter.view.MainActivity
import com.king.dexmorphhunter.view.adapter.AppListAdapter
import com.king.dexmorphhunter.view.adapter.ArgumentsListAdapter
import com.king.dexmorphhunter.view.adapter.MethodListAdapter
import com.king.dexmorphhunter.viewmodel.AppListViewModel
import com.king.dexmorphhunter.viewmodel.AppListViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

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
    fun provideAppRepository(appDatabase: AppDatabase): AppRepository {

        return AppRepository(appDatabase)
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
            @ApplicationContext context: Context,
            ClassAndMethodList: MutableList<String>
        ): MethodListAdapter {
            return MethodListAdapter(context,ClassAndMethodList)
        }

    @Provides
    fun provideArgumentsListAdapter(
        argumentList: List<ArgumentInfo>
    ): ArgumentsListAdapter {
        return ArgumentsListAdapter(argumentList)
    }

}

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
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
}
