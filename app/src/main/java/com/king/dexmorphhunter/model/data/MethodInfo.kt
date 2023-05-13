package com.king.dexmorphhunter.model.data

data class MethodInfo(
    val methodName: String,
    val arguments: List<ArgumentInfo>? = null,
    val returnType: Class<*>? = null,
    val returnValue: Any? = null
)