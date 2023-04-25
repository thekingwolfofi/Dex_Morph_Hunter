@file:Suppress("DEPRECATION")
package com.king.dexmorphhunter.model.util

import android.content.Context
import android.content.pm.PackageManager
import dalvik.system.DexFile

object PackageUtils {
    /*
    private var convertDataUtils: ConvertDataUtils = ConvertDataUtils()

    fun onExistCache(context: Context,packageName: String):Boolean{
        // Instancia o cache do aplicativo para class_info
        val prefs = context.getSharedPreferences("class_info", Context.MODE_PRIVATE)
        return prefs.getString("${packageName}_class_info", null)!= null
    }


    fun getClassListFromCache(context: Context, packageName: String): List<ClassInfo> {
        val prefs = context.getSharedPreferences("class_info", Context.MODE_PRIVATE)
        val classesString = prefs.getString("${packageName}_class_info", null)

        return if (classesString != null) {
            convertDataUtils.stringToClassInfo(classesString)
        } else {
            emptyList()
        }
    }
    */
    fun getListClassesInPackage(context: Context, packageName: String): List<String> {
        // Lista de classes que serão retornadas pela função
        val classes = mutableListOf<String>()

        // Instancia o cache do aplicativo para class_info
        //val prefs = context.getSharedPreferences("class_info", Context.MODE_PRIVATE)
        // Instancia o editor de cache
        //val editor = prefs.edit()

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

}