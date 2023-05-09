package com.king.dexmorphhunter.model.data

data class ArgumentInfo(
    val name: String,
    val type: Class<*>? = null,
    val value: Any? = null)
