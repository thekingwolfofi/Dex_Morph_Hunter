@file:Suppress("DEPRECATION")

package com.king.dexmorphhunter.model

import android.content.ContentValues.TAG
import android.util.Log
import dalvik.system.DexFile
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.io.IOException

class MethodInfoExtractModule(private val packageName: String) : IXposedHookLoadPackage {
    private val methodNames = mutableListOf<String>()
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName == packageName) {
            // Obter todas as classes do aplicativo alvo
            val classLoader = lpparam.classLoader
            val classes = ArrayList<Class<*>>()
            val classPath = System.getProperty("java.class.path")
            val entries = classPath?.split(":")
            if (entries != null) {
                for (entry in entries) {
                    try {
                        val dexFile = DexFile(entry)
                        val enumeration = dexFile.entries()
                        while (enumeration.hasMoreElements()) {
                            val className = enumeration.nextElement() as String
                            if (className.startsWith(lpparam.packageName)) {
                                classes.add(classLoader.loadClass(className))
                            }
                        }
                    } catch (e: IOException) {
                        Log.e(TAG, "Error getting classes from dex file", e)
                    }
                }
            }
            // Agora você pode iterar sobre a lista de classes e fazer o que quiser com elas
            for (clazz in classes) {
                // faça algo com a classe, como obter seus métodos, campos, etc.
                val methodList = clazz.declaredMethods
                      (method in methodList){
                    this.methodNames.add(method.name)
                }
            }
        }
    }

}

    