package com.king.dexmorphhunter.model.db

import androidx.room.*
import com.king.dexmorphhunter.model.data.ClassInfo

@Dao
interface ClassInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(classInfo: List<ClassInfo>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(classInfo: ClassInfo)

    @Update
    suspend fun update(classInfo: ClassInfo)

    @Delete
    suspend fun delete(classInfo: ClassInfo)

    @Query("SELECT * FROM class_info")
    suspend fun getAll(): List<ClassInfo>

    @Query("SELECT * FROM class_info WHERE package_name = :packageName")
    suspend fun getByPackageName(packageName: String): List<ClassInfo>?

    @Query("DELETE FROM class_info")
    suspend fun deleteAll()

}
