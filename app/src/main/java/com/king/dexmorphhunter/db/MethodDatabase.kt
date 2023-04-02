package com.king.dexmorphhunter.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.lang.reflect.Method

@Database(entities = [Method::class], version = 1, exportSchema = false)
abstract class MethodDatabase : RoomDatabase() {

    abstract fun methodDao(): MethodDao

    private class MethodDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val methodDao = database.methodDao()

                    // Adiciona métodos iniciais ao banco de dados
                    methodDao.insertAll(*getInitialMethods())
                }
            }
        }
    }

    companion object {

        @Volatile
        private var INSTANCE: MethodDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): MethodDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MethodDatabase::class.java,
                    "method_database"
                )
                    .addCallback(MethodDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private fun getInitialMethods(): Array<Method> {
            return arrayOf(
                Method(
                    "com.example.myapplication.MyClass",
                    "myMethod1",
                    "MyClass.myMethod1() was called"
                ),
                Method(
                    "com.example.myapplication.MyClass",
                    "myMethod2",
                    "MyClass.myMethod2() was called"
                )
            )
        }
    }
}
