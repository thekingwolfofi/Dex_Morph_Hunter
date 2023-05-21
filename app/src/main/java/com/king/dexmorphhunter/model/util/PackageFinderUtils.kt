@file:Suppress("DEPRECATION")
package com.king.dexmorphhunter.model.util

import android.content.Context
import android.content.pm.PackageManager
import com.king.dexmorphhunter.model.data.ArgumentInfo
import dalvik.system.DexFile
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import java.lang.reflect.Method

object PackageFinderUtils {

    private const val testMode = true

    // Este método retorna uma lista de nomes de classe para o packagename determinado
    fun getListClassesInPackage(context: Context, packageName: String): List<String> {
        // Lista de classes que serão retornadas pela função
        val classes = mutableListOf<String>()

        try {
            // Contexto do pacote desejado
            val packageContext = context.createPackageContext(packageName, Context.CONTEXT_IGNORE_SECURITY)

            // Gerenciador de pacotes do sistema
            val pm = packageContext.packageManager


            // Obtém informações sobre o pacote desejado
            val packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)

            // Diretório onde o arquivo APK do pacote está instalado
            val appDir = packageInfo.applicationInfo.sourceDir

            // Objeto DexFile que permite carregar as classes do arquivo APK
            val dexFile = DexFile(appDir)

            // Itera sobre todas as entradas (classes) no arquivo APK
            val entries = dexFile.entries()
            while (entries.hasMoreElements()) {
                // Nome completo da classe (incluindo o pacote)
                val className = entries.nextElement()

                // Verifica se a classe pertence ao pacote desejado
                if (className.startsWith(packageName)) {
                    // Adiciona a classe à lista de classes
                    classes.add(className)

                }
            }
        } catch (e: Exception) {
            // Em caso de erro, imprime a exceção para facilitar a depuração
            e.printStackTrace()
        }

        // Retorna a lista de classes encontradas
        return classes
    }

    // Este método retorna uma lista de nomes de método para uma determinada classe
    private fun getMethodList(clazz: Class<*>): List<Method> {
        val methodInstances = mutableListOf<Method>()
        for (method in clazz.declaredMethods) {
            methodInstances.add(method)
        }
        return methodInstances
    }

    // Este método público pode ser chamado de outras partes do código para obter a lista de nomes de método
    fun getAllMethods(className: String): List<Method> {
        if (testMode){
            val testClass = Class.forName("com.king.dexmorphhunter.model.Test")
            val methodInstances = mutableListOf<Method>()
            for (method in testClass.declaredMethods) {
                methodInstances.add(method)
            }
            return methodInstances
        } else {
            return try {
                val clazz = XposedHelpers.findClass(className, null)
                getMethodList(clazz)
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    private fun getArgumentsInfo(methodName: String, targetClass: Class<*>, packageName: String): List<ArgumentInfo>? {
        val method = targetClass.methods.firstOrNull { it.name == methodName }
        if (method != null) {
            XposedBridge.hookMethod(method, object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    val args = param.args // Array de argumentos do método
                    val argumentInfoList = mutableListOf<ArgumentInfo>()

                    for (i in args.indices) {
                        val argumentName = "arg$i"
                        val argumentType = args[i]?.javaClass ?: Any::class.java
                        val argumentValue = args[i]?.toString() ?: "null"

                        val argumentInfo = ArgumentInfo(argumentName, method.name, packageName, argumentType, argumentValue)
                        argumentInfoList.add(argumentInfo)
                    }

                    // Use a lista de ArgumentInfo aqui, se necessário
                }
            })
        }

        return null
    }


}