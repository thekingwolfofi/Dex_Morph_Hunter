package com.king.dexmorphhunter.xposed

import com.king.dexmorphhunter.model.data.ArgumentInfo
import com.king.dexmorphhunter.model.data.MethodInfo
import com.king.dexmorphhunter.model.repository.AppRepository
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import kotlinx.coroutines.*
import java.lang.reflect.Parameter
import javax.inject.Inject

class MethodExtractorXposedModule : IXposedHookLoadPackage {
    @Inject
    lateinit var appRepository: AppRepository
    private var methodInfoList: List<MethodInfo>? = null

    @OptIn(DelicateCoroutinesApi::class)
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        GlobalScope.launch(Dispatchers.IO) {

            if (methodInfoList == null) {
                methodInfoList = getMethodData()
            }

            methodInfoList?.let { list ->
                if (list.isNotEmpty() && lpparam != null) {
                    for (method in list) {
                        if (lpparam.packageName == method.packageName && method.isInterceptedMethod) {
                            interceptMethod(method, lpparam)
                        }
                    }
                }
            }
        }
    }

    private fun interceptMethod(method: MethodInfo, lpparam: XC_LoadPackage.LoadPackageParam) {
        val classLoader = XposedHelpers.findClassIfExists(method.className, lpparam.classLoader)
        if (classLoader != null) {
            val targetMethod = XposedHelpers.findMethodExactIfExists(classLoader, method.methodName)
            if (targetMethod != null) {
                XposedBridge.hookMethod(targetMethod, object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        runBlocking(Dispatchers.IO) {
                            val argumentInfoList = getArgumentInfoListByClassNameAndMethodName(method.className, method.methodName)
                            if (argumentInfoList == null) {
                                val parameters = targetMethod.parameters
                                val args = param.args
                                val newArguments = mutableListOf<ArgumentInfo>()

                                for (parameter in parameters) {
                                    val parameterName = parameter.name
                                    val parameterType = parameter.type
                                    val argumentValue = findArgumentValueByParameter(parameter, args, parameters.indexOf(parameter))

                                    val newArgument = ArgumentInfo(
                                        argumentName = parameterName ?: "arg${parameters.indexOf(parameter)}_${parameterType.simpleName}",
                                        methodName = method.methodName,
                                        packageName = method.packageName,
                                        argumentType = parameterType,
                                        argumentValue = argumentValue,
                                        newArgumentValue = null
                                    )
                                    newArguments.add(newArgument)
                                }

                                // Salve os novos argumentos no banco de dados
                                setArgumentInfoList(newArguments)
                            } else {
                                val args = argumentInfoList.map {
                                    it.newArgumentValue ?: it.argumentValue
                                }.toTypedArray()
                                param.args = args
                            }
                        }
                    }

                    override fun afterHookedMethod(param: MethodHookParam) {
                        runBlocking(Dispatchers.IO) {
                            if (method.methodReturnValue == null) {
                                updateMethodReturnValue(
                                    method.className,
                                    method.methodName,
                                    param.result
                                )
                            }
                        }
                        if (method.changeReturnMethod) {
                            param.result = method.newMethodReturnValue
                        }
                    }

                })
            }
        }
    }

    private suspend fun getMethodData(): List<MethodInfo> = withContext(Dispatchers.IO) {
        return@withContext appRepository.getAllMethodList()
    }

    private suspend fun getArgumentInfoListByClassNameAndMethodName(className: String, methodName: String): List<ArgumentInfo>? = withContext(Dispatchers.IO) {
        return@withContext appRepository.getArgumentInfoListByClassNameAndMethodName(className, methodName)

    }

    private suspend fun setArgumentInfoList(argumentInfoList: List<ArgumentInfo>) = withContext(Dispatchers.IO) {
        appRepository.setArgumentInfoList(argumentInfoList)
    }

    fun findArgumentValueByParameter(parameter: Parameter, args: Array<Any>, parameterIndex: Int): Any? {
        for (i in args.indices) {
            val arg = args[i]
            if (parameter.type.isInstance(arg) && i == parameterIndex) {
                return arg
            }
        }
        return null
    }


    private suspend fun updateMethodReturnValue(
        className: String,
        methodName: String,
        returnValue: Any?
    ) {
        appRepository.updateMethodReturnValue(className, methodName, returnValue)
    }
    

}
