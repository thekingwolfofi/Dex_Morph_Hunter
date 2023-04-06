package com.king.dexmorphhunter.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.king.dexmorphhunter.databinding.ActivityMainBinding
import com.king.dexmorphhunter.viewmodel.AppListViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val appListAdapter = AppListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val appListViewModel = ViewModelProvider(this).get(AppListViewModel::class.java)

        appListViewModel.loadAppList(this)

        binding.appListView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = appListAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            clipToPadding = false
            setPadding(0, 16, 0, 16)
        }

        appListViewModel.appList.observe(this) { appList ->
            appListAdapter.updateList(appList)
        }

        binding.interceptedAppsSwitch.setOnCheckedChangeListener { _, isChecked ->
            appListViewModel.onInterceptedAppsSwitchCheckedChanged(isChecked)
        }

        binding.systemAppsSwitch.setOnCheckedChangeListener { _, isChecked ->
            appListViewModel.onSystemAppsSwitchCheckedChanged(isChecked)
        }
    }
}