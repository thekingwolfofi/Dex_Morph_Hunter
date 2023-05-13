package com.king.dexmorphhunter.model.data

data class ClassInfo(
    val packageClass: String,
    val methodsIntercepted: List<MethodInfo>? = null
)