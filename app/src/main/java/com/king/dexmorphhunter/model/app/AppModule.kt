package com.king.dexmorphhunter.model.app

import android.content.Context
import android.graphics.Bitmap
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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

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
        appSettingsDao: AppSettingsDao,
        appInfoDao:AppInfoDao
    ): AppRepository {

        return AppRepository(appSettingsDao, appInfoDao)
    }

    @Provides
    fun providePackageRemovedReceiver(
        appSettingsDao: AppSettingsDao,
        appInfoDao: AppInfoDao
    ) : PackageRemovedReceiver {
        return PackageRemovedReceiver(appSettingsDao, appInfoDao)
    }

    @Provides
    fun provideAppListAdapter(
        @ApplicationContext context: Context,
        appListViewModel: AppListViewModel,
        updateIsIntercepted: (packageName: String, isIntercepted: Boolean) -> Unit,
        getBitmapFromPackage: (packageName: String) -> Bitmap?
    ): AppListAdapter {
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

    @Provides
    fun provideMainActivity(
            appListViewModel: AppListViewModel,
            appListAdapter: AppListAdapter,
            appRepository: AppRepository
    ):MainActivity{
        return MainActivity(appListViewModel, appListAdapter, appRepository)
    }


    @Provides
    fun provideContext(): Context {
        return App.instance.applicationContext
    }

    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java, "app-database"
        ).build()
    }

    @Provides
    fun provideAppSettingsDao(appDatabase: AppDatabase): AppSettingsDao{
        return appDatabase.appSettingsDao()
    }

    @Provides
    fun provideAppInfoDao(appDatabase: AppDatabase): AppInfoDao {
        return appDatabase.appInfoDao()
    }

}