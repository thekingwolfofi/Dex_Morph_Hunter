package com.king.dexmorphhunter.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.king.dexmorphhunter.model.MethodInfo

@Dao
interface MethodDao {
    @Query("SELECT * FROM method WHERE package_name = :packageName")
    suspend fun getMethodsByPackageName(packageName: String): List<MethodInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMethods(methods: List<MethodInfo>)
}
