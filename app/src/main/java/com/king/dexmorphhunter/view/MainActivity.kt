package com.king.dexmorphhunter.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.king.dexmorphhunter.databinding.ActivityMainBinding
import com.king.dexmorphhunter.model.data.AppInfo
import com.king.dexmorphhunter.model.repository.AppRepository
import com.king.dexmorphhunter.view.adapter.AppListAdapter
import com.king.dexmorphhunter.viewmodel.AppListViewModel
import com.king.dexmorphhunter.viewmodel.AppListViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var viewModel: AppListViewModel
    @Inject lateinit var adapter: AppListAdapter
    @Inject lateinit var appRepository: AppRepository
    private lateinit var binding: ActivityMainBinding
    private lateinit var progressBar: ProgressBar

    private var appList: List<AppInfo> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Carrega a lista de aplicativos
        loadApps()

        viewModelSetup()

        bindingSetup()

    }

    private fun viewModelSetup(){

        // Inicializa o ViewModel
        val viewModelFactory = AppListViewModelFactory(applicationContext, appRepository)
        viewModel = ViewModelProvider(this, viewModelFactory)[AppListViewModel::class.java]

        // Observe as mudanças na lista de aplicativos
        viewModel.appList.observe(this) { newList ->
            appList = newList ?: emptyList()
            adapter.updateList(appList)
            //adapter = AppListAdapter(this, appList, viewModel::updateIsIntercepted,viewModel::getBitmapFromPackage)
            //binding.appListRecyclerView.adapter = adapter
        }

        viewModel.filterInterceptedApps.observe(this){
            filterApps()
        }

        viewModel.filterSystemApps.observe(this){
            filterApps()
        }

        viewModel.filterQueryApps.observe(this){
            filterApps()
        }
    }
    private fun bindingSetup(){
        
        // Inicializa a view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa o RecyclerView
        binding.appListRecyclerView.layoutManager = LinearLayoutManager(this)
        
        // adiciona o adapter ao recycleview
        binding.appListRecyclerView.adapter = adapter
        
        binding.interceptedAppsSwitch.setOnCheckedChangeListener { _, isChecked ->
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

        binding.systemAppsSwitch.setOnCheckedChangeListener { _, isChecked ->
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
                        viewModel.filterQueryApps(
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

    private fun filterApps(){
        val job = Job()
        val scope = CoroutineScope(Dispatchers.IO + job)
        scope.launch {
            viewModel.updateListApps()

        }
    }

}
