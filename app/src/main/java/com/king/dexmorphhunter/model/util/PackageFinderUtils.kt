@file:Suppress("DEPRECATION")
package com.king.dexmorphhunter.model.util

import android.content.Context
import android.content.pm.PackageManager
import dalvik.system.DexFile
import de.robv.android.xposed.XposedHelpers
import java.lang.reflect.Method

object PackageFinderUtils {

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
        val methodNames = mutableListOf<Method>()
        for (method in clazz.declaredMethods) {
            methodNames.add(method)
        }
        return methodNames
    }

    // Este método público pode ser chamado de outras partes do código para obter a lista de nomes de método
    fun getAllMethods(className: String): List<Method> {
        return try {
            val clazz = XposedHelpers.findClass(className, null)
            getMethodList(clazz)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList<Method>()
        }
    }

}