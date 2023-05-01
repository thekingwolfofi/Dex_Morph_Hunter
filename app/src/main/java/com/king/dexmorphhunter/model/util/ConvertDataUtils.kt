package com.king.dexmorphhunter.model.util

class ConvertDataUtils {
    /*    @Suppress("NAME_SHADOWING")
    fun stringToClassInfo(classesString: String): List<ClassInfo> {
        val classesList = classesString.split(", ")
        return classesList.map { classString ->
            val classInfoParts = classString.split("::")
            val className = classInfoParts[0]
            val methodInfoString = classInfoParts.getOrNull(1)
            val argumentInfoString = classInfoParts.getOrNull(2)

            val methodInfo = if (methodInfoString != null) {
                val methodInfoParts = methodInfoString.split(";")
                val methodName = methodInfoParts[0]
                val returnType = methodInfoParts.getOrNull(1)?.let { Class.forName(it) }
                val argumentsString = methodInfoParts.getOrNull(2)
                val arguments = if (argumentsString != null) {
                    val argumentList = argumentsString.split("|")
                    argumentList.map { argumentString ->
                        val argumentParts = argumentString.split(":")
                        val argumentName = argumentParts[0]
                        val argumentType = Class.forName(argumentParts[1])
                        val argumentValue = argumentParts.getOrNull(2)?.let { valueString ->
                            when (argumentType) {
                                String::class.java -> valueString
                                Int::class.java -> valueString.toInt()
                                Boolean::class.java -> valueString.toBoolean()
                                Float::class.java -> valueString.toFloat()
                                Double::class.java -> valueString.toDouble()
                                Long::class.java -> valueString.toLong()
                                else -> null
                            }
                        }
                        ArgumentInfo(argumentName, argumentType, argumentValue)
                    }
                } else {
                    null
                }
                MethodInfo(methodName, arguments, returnType)
            } else {
                null
            }

            val argumentsInfo = argumentInfoString?.let { argumentInfoString ->
                val argumentsList = argumentInfoString.split("|")
                argumentsList.map { argumentString ->
                    val argumentParts = argumentString.split(":")
                    val argumentName = argumentParts[0]
                    val argumentType = Class.forName(argumentParts[1])
                    val argumentValue = argumentParts.getOrNull(2)?.let { valueString ->
                        when (argumentType) {
                            String::class.java -> valueString
                            Int::class.java -> valueString.toInt()
                            Boolean::class.java -> valueString.toBoolean()
                            Float::class.java -> valueString.toFloat()
                            Double::class.java -> valueString.toDouble()
                            Long::class.java -> valueString.toLong()
                            else -> null
                        }
                    }
                    ArgumentInfo(argumentName, argumentType, argumentValue)
                }
            }
            ClassInfo(className, methodInfo?.let { listOf(it) }, argumentsInfo)
        }
    }
    */
}