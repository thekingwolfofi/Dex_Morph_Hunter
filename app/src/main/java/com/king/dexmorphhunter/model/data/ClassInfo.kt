package com.king.dexmorphhunter.model.data

import androidx.room.*

@Entity(tableName = "class_info" )
data class ClassInfo(
    @PrimaryKey
    @ColumnInfo(name = "class_name")
    val className: String,
    @ColumnInfo(name = "package_name")
    val packageName: String
)
