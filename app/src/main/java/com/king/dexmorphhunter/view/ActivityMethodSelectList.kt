package com.king.dexmorphhunter.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.king.dexmorphhunter.databinding.ActivityMethodSelectListBinding
import com.king.dexmorphhunter.model.AppInfo
import com.king.dexmorphhunter.viewmodel.MethodSelectViewModel

class ActivityMethodSelectList : AppCompatActivity() {
    private lateinit var binding: ActivityMethodSelectListBinding
    private val viewModel: MethodSelectViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMethodSelectListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val appInfo = intent.getParcelableExtra<AppInfo>(AppSelectList.APP_INFO_EXTRA)!!

        viewModel.extractMethodsAndClasses(appInfo.packageName)

        viewModel.methodInfoList.observe(this, { methodInfoList ->
            val classList = methodInfoList.groupBy { it.className }.map { it.key }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, classList)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            binding.classSpinner.adapter = adapter

            binding.classSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    val selectedClass = parent.getItemAtPosition(position).toString()
                    val methodList = methodInfoList.filter { it.className == selectedClass }.map { it.methodName }

                    val methodAdapter = ArrayAdapter(this@ActivityMethodSelectList, android.R.layout.simple_spinner_item, methodList)
                    methodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    binding.methodSpinner.adapter = methodAdapter
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Não é necessário fazer nada quando nenhum item é selecionado
                }
            }
        })

        binding.startButton.setOnClickListener {
            val selectedClass = binding.classSpinner.selectedItem.toString()
            val selectedMethod = binding.methodSpinner.selectedItem.toString()

            viewModel.startInterceptService(selectedClass, selectedMethod)
        }
    }
}
