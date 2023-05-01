package com.king.dexmorphhunter.model.db

data class ArgumentInfo(
    val name: String,
    val type: Class<*>? = null,
    val value: Any? = null)
