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

    @Query("SELECT * FROM method_info WHERE class_name = :className")
    suspend fun getByClassName(className: String): List<MethodInfo>?

    @Query("DELETE FROM method_info")
    suspend fun deleteAll()

    @Query("SELECT * FROM method_info WHERE class_name = :className AND method_name = :methodName")
    suspend fun getByClassNameAndMethodName(className: String, methodName: String): MethodInfo?

    @Query("UPDATE method_info SET is_intercepted_method = :isIntercepted WHERE class_name = :className AND method_name = :methodName")
    suspend fun updateIsInterceptedByClassNameAndMethodName(className: String, methodName: String, isIntercepted: Boolean)


    @Query("UPDATE method_info SET method_return_value = :returnValue WHERE class_name = :className AND method_name = :methodName")
    suspend fun updateReturnValueByClassNameAndMethodName(className: String, methodName: String, returnValue: Any?)

    @Query("UPDATE method_info SET change_return_method = :changeReturnMethod WHERE class_name = :className AND method_name = :methodName")
    suspend fun updateChangeReturnByClassNameAndMethodName(className: String, methodName: String, changeReturnMethod: Boolean)


}

