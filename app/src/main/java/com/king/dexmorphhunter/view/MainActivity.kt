package com.king.dexmorphhunter.view

import android.annotation.SuppressLint
import android.content.Context
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

@Suppress("NAME_SHADOWING")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: AppListViewModel
    private lateinit var adapter: AppListAdapter
    private lateinit var progressBar: ProgressBar

    private var appList: List<AppInfo> = emptyList()

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPrefs = this.getSharedPreferences("app_cache", Context.MODE_PRIVATE)

        // Inicializa a view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa o ViewModel
        val appListModel = AppListModel(applicationContext)
        val viewModelFactory = AppListViewModelFactory(applicationContext, appListModel)
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
            adapter = AppListAdapter(this, appList, viewModel::updateIsIntercepted,viewModel::getBitmapFromPackage)
            binding.appListRecyclerView.adapter = adapter
        }

        binding.interceptedAppsSwitch.setOnCheckedChangeListener { _, isChecked ->
            val sharedPrefs = this.getSharedPreferences("app_cache", Context.MODE_PRIVATE)
            val editor = sharedPrefs.edit()
            editor.putBoolean("interceptedAppsSwitch",isChecked)
            val job = Job()
            val scope = CoroutineScope(Dispatchers.Main + job)
            scope.launch {
                binding.progressBar.visibility = View.VISIBLE
                withContext(Dispatchers.Default) {
                    viewModel.filterInterceptedApps(isChecked)
                }
                binding.progressBar.visibility = View.GONE
            }
        }

        val interceptedSwitchCache = sharedPrefs.getBoolean("interceptedAppsSwitch",false)

        binding.interceptedAppsSwitch.isChecked = interceptedSwitchCache

        binding.systemAppsSwitch.setOnCheckedChangeListener { _, isChecked ->
            val sharedPrefs = this.getSharedPreferences("app_cache", Context.MODE_PRIVATE)
            val editor = sharedPrefs.edit()
            editor.putBoolean("systemAppsSwitch",isChecked)
            val job = Job()
            val scope = CoroutineScope(Dispatchers.Main + job)
            scope.launch {
                binding.progressBar.visibility = View.VISIBLE
                withContext(Dispatchers.Default) {
                    viewModel.filterSystemApps(isChecked)
                }
                binding.progressBar.visibility = View.GONE
            }
        }


        val systemSwitchCache = sharedPrefs.getBoolean("systemAppsSwitch",false)

        binding.systemAppsSwitch.isChecked = systemSwitchCache

        binding.swipeRefreshLayout.setOnRefreshListener {
            val job = Job()
            val scope = CoroutineScope(Dispatchers.Main + job)
            scope.launch {
                binding.swipeRefreshLayout.isRefreshing = true
                withContext(Dispatchers.Default) {
                    viewModel.invalidateCache()
                }
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }

        progressBar = binding.progressBar

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val job = Job()
                val scope = CoroutineScope(Dispatchers.Main + job)
                scope.launch {
                    binding.progressBar.visibility = View.VISIBLE
                    withContext(Dispatchers.Default) {
                        viewModel.filterApps(
                            query
                        )
                    }
                    binding.progressBar.visibility = View.GONE
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

    }

    private fun loadApps() {
        val job = Job()
        val scope = CoroutineScope(Dispatchers.Main + job)
        scope.launch {
            binding.progressBar.visibility = View.VISIBLE
            withContext(Dispatchers.Default) {
                viewModel.getInstalledAppList()
            }
            binding.progressBar.visibility = View.GONE
        }
    }

}
