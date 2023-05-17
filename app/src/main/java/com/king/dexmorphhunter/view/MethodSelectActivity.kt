package com.king.dexmorphhunter.view

//noinspection SuspiciousImport
import android.R
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.king.dexmorphhunter.databinding.ActivityMethodListSelectBinding
import com.king.dexmorphhunter.model.data.ClassInfo
import com.king.dexmorphhunter.model.data.MethodInfo
import com.king.dexmorphhunter.model.repository.AppRepository
import com.king.dexmorphhunter.view.adapter.MethodListAdapter
import com.king.dexmorphhunter.viewmodel.MethodSelectViewModel
import com.king.dexmorphhunter.viewmodel.MethodSelectViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject


@Suppress("NAME_SHADOWING")
@AndroidEntryPoint
class MethodSelectActivity : AppCompatActivity() {

    @Inject lateinit var viewModel: MethodSelectViewModel
    @Inject lateinit var adapter: MethodListAdapter
    @Inject lateinit var appRepository: AppRepository
    private lateinit var binding: ActivityMethodListSelectBinding

    private var classList: List<ClassInfo> = emptyList()
    private var methodList: List<MethodInfo> = emptyList()

    private lateinit var classInfo: ClassInfo
    private lateinit var methodInfo: MethodInfo
    private var packageName: String = ""

    private var classSpinnerOptions: List<String> = emptyList()
    private var methodSpinnerOptions: List<String> = emptyList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        if (intent != null) {
            packageName  = intent.getStringExtra("packageName").toString()
        } else {
            // Tratar o caso em que o Intent é nulo
        }
        // Carrega a lista de aplicativos
        loadClasses()

        viewModelSetup()

        bindingSetup()

    }

    private fun viewModelSetup(){

        // Inicializa o ViewModel
        val viewModelFactory = MethodSelectViewModelFactory(applicationContext, appRepository)
        viewModel = ViewModelProvider(this, viewModelFactory)[MethodSelectViewModel::class.java]

        // Observe as mudanças na lista de Classes
        viewModel.classList.observe(this) { newList ->
            classList = newList ?: emptyList()
            classSpinnerOptions = classList.map { it.className }
            binding.classesSpinner.adapter = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, classSpinnerOptions)
        }

        // Observe as mudanças na lista de Metodos
        viewModel.methodList.observe(this) { newList ->
            methodList = newList ?: emptyList()
            methodSpinnerOptions = methodList.map { it.methodName }
            binding.addArgumentButton.isEnabled = !methodList.map { it.methodName }.contains("Xposed não encontrado")
            binding.methodSpinner.adapter = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, methodSpinnerOptions)
        }



    }
    private fun bindingSetup(){

        // Inicializa a view binding
        binding = ActivityMethodListSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa o RecyclerView
        binding.methodSelectListRecyclerView.layoutManager = LinearLayoutManager(applicationContext)

        // adiciona o adapter ao recycleview
        binding.methodSelectListRecyclerView.adapter = adapter

        binding.filterButton.setOnClickListener {
            val job = Job()
            val scope = CoroutineScope(Dispatchers.Main + job)
            scope.launch {
                binding.progressBar.visibility = View.VISIBLE
                withContext(Dispatchers.Default) {
                    viewModel.setFilterClass(packageName)
                }
                binding.progressBar.visibility = View.GONE
            }
        }

        binding.addArgumentButton.setOnClickListener {
            //viewModel.addToSelectedList(className, methodName)
            adapter.addItem(classInfo, methodInfo)
        }

        binding.classesSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                classInfo = classList[position]
                loadMethods()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.methodSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                methodInfo = methodList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.titleTextView.text = packageName

    }

    private fun loadClasses() {
        val job = Job()
        val scope = CoroutineScope(Dispatchers.Main + job)
        scope.launch {
            binding.progressBar.visibility = View.VISIBLE
            withContext(Dispatchers.Default) {
                packageName.let { viewModel.getClassList(it) }
            }
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun loadMethods() {
        val job = Job()
        val scope = CoroutineScope(Dispatchers.Main + job)
        scope.launch {
            binding.progressBar.visibility = View.VISIBLE
            withContext(Dispatchers.Default) {
                viewModel.getMethodList(classInfo)
            }
            binding.progressBar.visibility = View.GONE
        }
    }

}

