package com.king.dexmorphhunter.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.king.dexmorphhunter.databinding.ActivityMainBinding
import com.king.dexmorphhunter.viewmodel.MyViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSwitches()

        val adapter = AppListAdapter(viewModel)
        binding.recyclerView.adapter = adapter

        viewModel.installedAppsList.observe(this) { appList ->
            adapter.submitList(appList)
        }

    }

    private fun setupSwitches() {
        binding.interceptedAppsSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onInterceptedAppsSwitchCheckedChanged(isChecked)
        }
        binding.systemAppsSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onSystemAppsSwitchCheckedChanged(isChecked)
        }
        viewModel.updateAppList()
    }
}
