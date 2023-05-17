package com.king.dexmorphhunter.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "argument_info")
data class ArgumentInfo(
    @PrimaryKey
    @ColumnInfo(name = "argument_name")
    val argumentName: String,
    @ColumnInfo(name = "method_name")
    val methodName: String,
    @ColumnInfo(name = "package_name")
    val packageName: String,
    @ColumnInfo(name = "argument_type")
    val argumentType: Class<*>,
    @ColumnInfo(name = "argument_value")
    val argumentValue: Any? = null,
    @ColumnInfo(name = "new_argument_value")
    val newArgumentValue: Any? = null
)
