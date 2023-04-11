package com.king.dexmorphhunter.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.king.dexmorphhunter.databinding.ActivityMainBinding
import com.king.dexmorphhunter.model.AppListModel
import com.king.dexmorphhunter.view.adapter.AppListAdapter
import com.king.dexmorphhunter.viewmodel.AppListViewModel
import com.king.dexmorphhunter.viewmodel.AppListViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: AppListViewModel
    private lateinit var adapter: AppListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa a view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa o ViewModel
        val appListModel = AppListModel(applicationContext)
        val viewModelFactory = AppListViewModelFactory(appListModel)
        viewModel = ViewModelProvider(this, viewModelFactory)[AppListViewModel::class.java]

        // Carrega a lista de aplicativos
        viewModel.loadInstalledApps(binding.interceptedAppsSwitch.isChecked, binding.systemAppsSwitch.isChecked)

        // Inicializa o RecyclerView
        binding.appListRecyclerView.layoutManager = LinearLayoutManager(this)

        // Adiciona um item de decoração ao RecyclerView (opcional)
        binding.appListRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        // Observe as mudanças na lista de aplicativos
        viewModel.appList.observe(this) { appList ->
            // Inicializa o Adapter
            adapter = AppListAdapter(this, appList, viewModel::updateInterceptedApp)
            // Atribui o Adapter ao RecyclerView
            binding.appListRecyclerView.adapter = adapter
        }

        val qua = binding.searchView.queryHint.toString()

        binding.interceptedAppsSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.loadInstalledApps(isChecked, binding.systemAppsSwitch.isChecked)

        }

        binding.systemAppsSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.loadInstalledApps(binding.interceptedAppsSwitch.isChecked, isChecked)

        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.filterApps(
                    binding.interceptedAppsSwitch.isChecked,
                    binding.systemAppsSwitch.isChecked,
                    query
                )
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })


    }

}
