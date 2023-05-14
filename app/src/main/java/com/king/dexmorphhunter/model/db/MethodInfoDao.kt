package com.king.dexmorphhunter.model.db

import androidx.room.*
import com.king.dexmorphhunter.model.data.MethodInfo

@Dao
interface MethodInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(methodInfo: List<MethodInfo>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(methodInfo: MethodInfo)

    @Update
    suspend fun update(methodInfo: MethodInfo)

    @Delete
    suspend fun delete(methodInfo: MethodInfo)

    @Query("SELECT * FROM method_info")
    suspend fun getAll(): List<MethodInfo>

    @Query("SELECT * FROM method_info WHERE package_name = :packageName")
    suspend fun getByPackageName(packageName: String): List<MethodInfo>?

    @Query("DELETE FROM method_info")
    suspend fun deleteAll()

}

