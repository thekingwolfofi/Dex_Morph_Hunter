package com.king.dexmorphhunter.model.repository

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.lifecycle.ViewModel
import com.king.dexmorphhunter.model.data.*
import com.king.dexmorphhunter.model.db.*
import com.king.dexmorphhunter.model.util.Constants
import com.king.dexmorphhunter.model.util.PackageFinderUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@Suppress("NAME_SHADOWING", "DEPRECATION")
@SuppressLint("StaticFieldLeak")
@HiltViewModel
class AppRepository @Inject constructor(
    val context: Context,
    appDatabase: AppDatabase
) : ViewModel() {

    private var appSettingsDao: AppSettingsDao = appDatabase.appSettingsDao()
    private var appInfoDao: AppInfoDao = appDatabase.appInfoDao()
    private var methodInfoDao: MethodInfoDao = appDatabase.methodInfoDao()
    private var classInfoDao: ClassInfoDao = appDatabase.classInfoDao()
    private var argumentInfoDao: ArgumentInfoDao = appDatabase.argumentInfoDao()

    private var isFiltred: Boolean = true

    @SuppressLint("QueryPermissionsNeeded")
    suspend fun loadInstalledAppList(context: Context): List<AppInfo> =
        withContext(Dispatchers.Main) {
            val mAppList: List<AppInfo>
            setupConfig()
            val reloadCache = appInfoDao.getAll().isEmpty()
            if (reloadCache) {
                val pm: PackageManager by lazy { context.packageManager }
                val appList: MutableList<AppInfo> = mutableListOf()
                withContext(Dispatchers.IO) {
                    val packages = pm.getInstalledPackages(PackageManager.GET_META_DATA)
                    for (packageInfo in packages) {
                        if (pm.getApplicationLabel(packageInfo.applicationInfo)
                                .toString() in Constants.removePackage
                        ) {
                            continue
                        }
                        val appName = pm.getApplicationLabel(packageInfo.applicationInfo).toString()
                        val isSystem = isSystemApp(context, packageInfo.packageName)
                        val isIntercepted =
                            appInfoDao.getByPackageName(packageInfo.packageName)?.isInterceptedApp
                                ?: false
                        appList.add(
                            AppInfo(
                                packageInfo.packageName,
                                appName,
                                isSystem,
                                isIntercepted
                            )
                        )

                    }
                    // Salva a lista de apps no cache com Room
                    insertAll(appList)

                    val isIntercepted =
                        appSettingsDao.getAppSettings()?.interceptedAppsSwitch ?: false
                    val isSystem = appSettingsDao.getAppSettings()?.systemAppsSwitch ?: false
                    mAppList = filterApps("", isIntercepted, isSystem)
                }
                return@withContext mAppList
            } else {
                withContext(Dispatchers.IO) {
                    val isIntercepted =
                        appSettingsDao.getAppSettings()?.interceptedAppsSwitch ?: false
                    val isSystem = appSettingsDao.getAppSettings()?.systemAppsSwitch ?: false
                    mAppList = filterApps("", isIntercepted, isSystem)
                }
                return@withContext mAppList
            }
        }

    private suspend fun setupConfig() {
        val settingsIsNull = getSettings()
        if (settingsIsNull) {
            withContext(Dispatchers.IO) {
                appSettingsDao.insertOrUpdateAppSettings(
                    AppSettings(
                        1,
                        interceptedAppsSwitch = false,
                        systemAppsSwitch = false
                    )
                )
            }
        }
    }

    suspend fun updateIsIntercepted(packageName: String, isIntercepted: Boolean) {
        // Atualiza o valor de `isInterceptedApp` no banco de dados
        appInfoDao.updateIsIntercepted(packageName, isIntercepted)


    }

    private suspend fun updateClasses(classList: List<ClassInfo>) {
        // Insere cada classe na lista
        classInfoDao.insertAll(classList)

    }

    private suspend fun updateMethods(MethodList: List<MethodInfo>) {
        // Insere cada classe na lista
        methodInfoDao.insertAll(MethodList)

    }

    suspend fun filterClassList(packageName: String): List<ClassInfo> = withContext(Dispatchers.IO) {
        val classList = classInfoDao.getByPackageName(packageName)
        if (classList.isNullOrEmpty()) {
            val classList = PackageFinderUtils.getListClassesInPackage(context, packageName)
                .filter { clazz -> !isFiltred || !clazz.contains("$") }
                .map { clazz -> ClassInfo(clazz, packageName) }
            updateClasses(classList)
            classList
        } else {
            classList
        }
    }

    suspend fun getMethodList(classInfo: ClassInfo): List<MethodInfo> = withContext(Dispatchers.IO) {
        val methodList = methodInfoDao.getByClassName(classInfo.className)
        return@withContext if (methodList.isNullOrEmpty()) {
            val methodList = PackageFinderUtils.getAllMethods(classInfo.className)
            if (methodList.isEmpty()) {
                listOf(MethodInfo("Xposed não encontrado", classInfo.packageName, classInfo.className))
            } else {
                val methodInfoList = methodList.map { method ->
                    MethodInfo(
                        method.name,
                        classInfo.packageName,
                        classInfo.className,
                        isInterceptedMethod = false,
                        changeReturnMethod = false,
                        method.returnType
                    )
                }
                updateMethods(methodInfoList)
                methodInfoList
            }
        } else {
            methodList
        }
    }


    fun setFilterClass() {
        isFiltred = !isFiltred
    }

    suspend fun invalidateCache(context: Context) {
        // Exclui todas as entradas do banco de dados
        appInfoDao.deleteAll()
        appSettingsDao.deleteAll()
        classInfoDao.deleteAll()
        methodInfoDao.deleteAll()
        argumentInfoDao.deleteAll()

        // Carrega a lista de aplicativos instalados
        loadInstalledAppList(context)
    }

    fun getBitmapFromPackage(context: Context, packageName: String): Bitmap? {
        return try {
            val packageManager = context.packageManager
            val appIcon = packageManager.getApplicationIcon(packageName) // Obter o AppIcon diretamente
            val appBitmap = Bitmap.createBitmap(
                appIcon.intrinsicWidth,
                appIcon.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(appBitmap)
            appIcon.setBounds(0, 0, canvas.width, canvas.height)
            appIcon.draw(canvas)
            appBitmap
        } catch (e: PackageManager.NameNotFoundException) {
            null
        } catch (e: Exception) {
            null
        }
    }

    private fun isSystemApp(context: Context, packageName: String): Boolean {
        val pm = context.packageManager
        val applicationInfo = pm.getApplicationInfo(packageName, 0)
        return packageName in Constants.importantPackagesList || applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }

    suspend fun filterApps(
        query: String? = "",
        isIntercepted: Boolean = false,
        isSystem: Boolean = false
    ): List<AppInfo> {
        return withContext(Dispatchers.IO) {
            appInfoDao.getFilterApps(query!!, isSystem, isIntercepted)
        }
    }

    private suspend fun insertAll(appInfoList: List<AppInfo>) = withContext(Dispatchers.IO) {
            appInfoDao.insertAll(appInfoList)
    }

    private suspend fun getSettings(): Boolean {
        return withContext(Dispatchers.IO) {
            val count = appSettingsDao.isAppSettingsTableEmpty()
            count == 0
        }
    }

    suspend fun updateSettings(appSettings: AppSettings) = withContext(Dispatchers.IO) {
            appSettingsDao.insertOrUpdateAppSettings(appSettings)
    }

    suspend fun updateMethodIsIntercepted(className: String, methodName: String, check: Boolean) = withContext(Dispatchers.IO) {
        methodInfoDao.updateIsInterceptedByClassNameAndMethodName(className, methodName, check)

    }

    suspend fun getByClassNameAndMethodName(
        className: String,
        methodName: String
    ): List<ArgumentInfo>? {
        return argumentInfoDao.getByClassNameAndMethodName(className, methodName)
    }

    suspend fun getAllMethodList(): List<MethodInfo> {
        return methodInfoDao.getAll()
    }

    suspend fun setArgumentInfoList(argumentInfoList: List<ArgumentInfo>) {
        argumentInfoDao.insertAll(argumentInfoList)
    }

    suspend fun updateMethodReturnValue(className: String, methodName: String, returnValue: Any?) {
        methodInfoDao.updateReturnValueByClassNameAndMethodName(className, methodName, returnValue)
    }

}