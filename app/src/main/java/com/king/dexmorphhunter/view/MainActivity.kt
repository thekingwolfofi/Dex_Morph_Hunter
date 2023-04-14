package com.king.dexmorphhunter.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.king.dexmorphhunter.databinding.ActivityMainBinding
import com.king.dexmorphhunter.model.AppListModel
import com.king.dexmorphhunter.model.db.AppInfo
import com.king.dexmorphhunter.view.adapter.AppListAdapter
import com.king.dexmorphhunter.viewmodel.AppListViewModel
import com.king.dexmorphhunter.viewmodel.AppListViewModelFactory
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: AppListViewModel
    private lateinit var adapter: AppListAdapter
    private lateinit var progressBar: ProgressBar

    private var appList: List<AppInfo> = emptyList()
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
        //viewModel.loadInstalledAppList()
        loadApps()

        // Inicializa o RecyclerView
        binding.appListRecyclerView.layoutManager = LinearLayoutManager(this)

        // Adiciona um item de decoração ao RecyclerView (opcional)
        binding.appListRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        // Observe as mudanças na lista de aplicativos
        viewModel.appList.observe(this) { newList ->
            appList = newList ?: emptyList()
            adapter = AppListAdapter(this, appList, viewModel::updateInterceptedApp,viewModel::getBitmapFromPackage)
            binding.appListRecyclerView.adapter = adapter
        }

        val quary = binding.searchView.queryHint.toString()


        binding.interceptedAppsSwitch.setOnCheckedChangeListener { _, isChecked ->
            loadApps()

        }

        binding.systemAppsSwitch.setOnCheckedChangeListener { _, isChecked ->
            //viewModel.loadInstalledAppList()
        }

        progressBar = binding.progressBar
    /*
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
    */

    }

    private fun loadApps() {
        val job = Job()
        val scope = CoroutineScope(Dispatchers.Main + job)
        scope.launch {
            binding.progressBar.visibility = View.VISIBLE
            val appList = withContext(Dispatchers.Default) {
                viewModel.loadInstalledAppList()
            }
            binding.progressBar.visibility = View.GONE
        }
    }

}
