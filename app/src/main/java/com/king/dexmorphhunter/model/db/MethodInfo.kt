package com.king.dexmorphhunter.model.db

import androidx.core.view.accessibility.AccessibilityViewCommand.CommandArguments

data class MethodInfo(
    val methodName: String,
    val arguments: List<ArgumentInfo>? = null,
    val returnType: Class<*>? = null)
