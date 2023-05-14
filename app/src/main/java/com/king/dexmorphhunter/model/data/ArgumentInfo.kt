package com.king.dexmorphhunter.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "argument_info")
data class ArgumentInfo(
    @PrimaryKey
    @ColumnInfo(name = "argument_name")
    val argumentName: String,
    @ColumnInfo(name = "argument_type")
    val argumentType: String,
    @ColumnInfo(name = "argument_value")
    val argumentValue: String? = null,
    @ColumnInfo(name = "package_name")
    val packageName: String
)
