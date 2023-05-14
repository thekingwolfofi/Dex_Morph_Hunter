package com.king.dexmorphhunter.model.data

import androidx.room.*

@Entity(tableName = "method_info")
data class MethodInfo(
    @PrimaryKey
    @ColumnInfo(name = "method_name")
    val methodName: String,
    @ColumnInfo(name = "method_return_type")
    val methodReturnType: Class<*>? = null,
    @ColumnInfo(name = "method_return_value")
    val methodReturnValue: Any? = null,
    @ColumnInfo(name = "package_name")
    val packageName: String
)
