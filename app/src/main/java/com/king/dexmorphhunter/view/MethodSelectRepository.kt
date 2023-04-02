package com.king.dexmorphhunter.model

import androidx.lifecycle.LiveData
import com.king.dexmorphhunter.db.MethodDao
import com.king.dexmorphhunter.db.MethodDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MethodSelectRepository(private val methodDao: MethodDao) {

    fun getAllMethods(): LiveData<List<Method>> {
        return methodDao.getAllMethods()
    }

    fun getMethodsByClass(className: String): LiveData<List<Method>> {
        return methodDao.getMethodsByClass(className)
    }

    suspend fun insertMethod(method: Method) = withContext(Dispatchers.IO) {
        methodDao.insert(method)
    }

    suspend fun deleteMethod(method: Method) = withContext(Dispatchers.IO) {
        methodDao.delete(method)
    }

    companion object {
        @Volatile private var instance: MethodSelectRepository? = null

        fun getInstance(database: MethodDatabase): MethodSelectRepository {
            return instance ?: synchronized(this) {
                instance ?: MethodSelectRepository(database.methodDao()).also { instance = it }
            }
        }
    }
}
