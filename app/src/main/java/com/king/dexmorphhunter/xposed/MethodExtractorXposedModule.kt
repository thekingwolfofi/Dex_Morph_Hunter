package com.king.dexmorphhunter.xposed

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class MethodExtractorXposedModule: IXposedHookLoadPackage {

    // Este método é chamado quando um pacote é carregado pelo sistema
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        // Aqui você pode verificar se o pacote que foi carregado é o que você deseja modificar.

    }

    // Este método retorna uma lista de nomes de método para uma determinada classe
    private fun getMethodNames(clazz: Class<*>): List<String> {
        val methodNames = mutableListOf<String>()
        for (method in clazz.declaredMethods) {
            methodNames.add(method.name)
        }
        return methodNames
    }

    // Este método público pode ser chamado de outras partes do código para obter a lista de nomes de método
    fun getAllMethodNames(className: String): List<String> {
        return try {
            val clazz = XposedHelpers.findClass(className, null)
            getMethodNames(clazz)
        } catch (e: Exception) {
            e.printStackTrace()
            listOf("Xposed não encontrado")
        }
    }

}

