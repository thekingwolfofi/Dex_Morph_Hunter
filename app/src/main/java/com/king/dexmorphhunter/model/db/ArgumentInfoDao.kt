package com.king.dexmorphhunter.model.db

import androidx.room.*
import com.king.dexmorphhunter.model.data.ArgumentInfo

@Dao
interface ArgumentInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(argumentInfo: List<ArgumentInfo>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(argumentInfo: ArgumentInfo)

    @Update
    suspend fun update(argumentInfo: ArgumentInfo)

    @Delete
    suspend fun delete(argumentInfo: ArgumentInfo)

    @Query("DELETE FROM argument_info")
    suspend fun deleteAll()

    @Query("SELECT * FROM argument_info WHERE package_name = :packageName AND method_name = :methodName")
    suspend fun getByClassNameAndMethodName(packageName: String, methodName: String): List<ArgumentInfo>?

}

