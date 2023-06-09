package com.king.dexmorphhunter.model.data

import androidx.room.*

@Entity(tableName = "method_info")
data class MethodInfo(
    @PrimaryKey
    @ColumnInfo(name = "method_name")
    val methodName: String,
    @ColumnInfo(name = "package_name")
    val packageName: String,
    @ColumnInfo(name = "class_name")
    val className: String,
    @ColumnInfo(name = "is_intercepted_method")
    val isInterceptedMethod: Boolean = false,
    @ColumnInfo(name = "change_return_method")
    val changeReturnMethod: Boolean = false,
    @ColumnInfo(name = "method_return_type")
    val methodReturnType: Class<*>? = null,
    @ColumnInfo(name = "method_return_value")
    val methodReturnValue: Any? = null,
    @ColumnInfo(name = "new_method_return_value")
    val newMethodReturnValue: Any? = null

)
