package com.king.dexmorphhunter.model.db

import android.util.Log
import androidx.room.*
import com.king.dexmorphhunter.model.data.AppInfo

@Dao
interface AppInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(appInfoList: List<AppInfo>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(appInfo: AppInfo)

    @Update
    suspend fun update(appInfo: AppInfo)

    @Delete
    suspend fun delete(appInfo: AppInfo)

    @Query("SELECT * FROM app_info")
    suspend fun getAll(): List<AppInfo>

    @Query("SELECT * FROM app_info WHERE packageName = :packageName")
    suspend fun getByPackageName(packageName: String): AppInfo?

    @Query("SELECT * FROM app_info WHERE (" +
            "(:query IS NULL OR :query = '') " +
            "OR LOWER(appName) LIKE '%' || LOWER(:query) || '%' " +
            "OR LOWER(packageName) LIKE '%' || LOWER(:query) || '%') " +
            "AND (is_system_app = 0 OR (is_system_app = 1 AND :isSystem = 1) OR :isIntercepted = 1) " +
            "AND ((:isIntercepted = 1 AND (is_intercepted_app = 1 AND :isIntercepted = 1)) OR :isIntercepted = 0) " +
            "ORDER BY appName ASC"
    )
    fun getFilterApps(query: String, isSystem: Boolean, isIntercepted: Boolean): List<AppInfo>

    @Query("DELETE FROM app_info")
    suspend fun deleteAll()

    @Query("UPDATE app_info SET is_intercepted_app = :isIntercepted WHERE packageName = :packageName")
    suspend fun updateIsIntercepted(packageName: String, isIntercepted: Boolean)

    fun testQuery(query: String, isSystem: Boolean, isIntercepted: Boolean): List<AppInfo> {
        val apps = getFilterApps(query, isSystem, isIntercepted)
        for(app in apps) {
            if(app.isSystemApp == true) {
                val interceptedApp = app.isInterceptedApp
                val systemApp = app.isSystemApp
                Log.d(
                    "QUERY_TEST",
                    "packageName é ${app.packageName}"
                )
                if (interceptedApp == isIntercepted) {
                    Log.d(
                        "QUERY_TEST",
                        "interceptedApp é igual a isIntercepted que é $isIntercepted"
                    )
                } else {
                    Log.d(
                        "QUERY_TEST",
                        "interceptedApp não é igual a isIntercepted que é $isIntercepted"
                    )
                }
                if (systemApp == isSystem) {
                    Log.d("QUERY_TEST", "systemApp é igual a isSystem que é $isSystem")
                } else {
                    Log.d("QUERY_TEST", "systemApp não é igual a isSystem que é $isSystem")
                }
                if ((systemApp == false || (systemApp == true && isSystem == true) || isIntercepted == true)) {
                    Log.d(
                        "QUERY_TEST",
                        "aprovou no (systemApp == false || (systemApp == true && isSystem == true) || isIntercepted == true) "
                    )
                } else {
                    Log.d(
                        "QUERY_TEST",
                        "reprovou no (systemApp == false || (systemApp == true && isSystem == true) || isIntercepted == true) "
                    )
                }
                if (((isIntercepted == true && (interceptedApp == true && isIntercepted == true)) || isIntercepted == false)) {
                    Log.d(
                        "QUERY_TEST",
                        "aprovou no ((isIntercepted == true && (interceptedApp == true && isIntercepted == true)) || isIntercepted == false) "
                    )
                } else {
                    Log.d(
                        "QUERY_TEST",
                        "reprovou no ((isIntercepted == true && (interceptedApp == true && isIntercepted == true)) || isIntercepted == false) "
                    )
                }
            }
        }

        return apps
    }

}
