package com.king.dexmorphhunter.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.king.dexmorphhunter.databinding.ActivityParameterEditorBinding
import com.king.dexmorphhunter.model.Test
import com.king.dexmorphhunter.model.db.ArgumentInfo
import com.king.dexmorphhunter.view.adapter.ArgumentsListAdapter
import com.king.dexmorphhunter.viewmodel.MethodSelectViewModel

class ParameterEditorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityParameterEditorBinding
    private lateinit var viewModel: MethodSelectViewModel
    private lateinit var adapter: ArgumentsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializa o contexto
        val context = applicationContext

        // Inicializa as variaveis passadas por instancia
        val arguments = getArgumentsInfo("testAllTypes", Test::class.java)

        // Inicializa a view binding
        binding = ActivityParameterEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = arguments?.let { ArgumentsListAdapter(it) }!!
        binding.recycleView.layoutManager = LinearLayoutManager(this)
        binding.recycleView.adapter = adapter

    }

    private fun getArgumentsInfo(methodName: String, targetClass: Class<*>): List<ArgumentInfo>? {
        val method = targetClass.methods.firstOrNull { it.name == methodName }
        if (method != null) {
            val parameters = method.parameters
            return parameters.mapIndexed { index, parameter ->
                val paramType = parameter.type
                val typeName = when {
                    paramType.isArray -> paramType.componentType.simpleName + "Array"
                    paramType.name.startsWith("java.") -> paramType.simpleName
                    else -> paramType.name.substringAfterLast('.')
                }
                val paramName = "arg$typeName"
                ArgumentInfo(paramName, paramType)
            }
        }
        return null
    }

}