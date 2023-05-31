package com.king.dexmorphhunter.view

//noinspection SuspiciousImport
import android.R
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.king.dexmorphhunter.databinding.ActivityMethodListSelectBinding
import com.king.dexmorphhunter.model.data.ClassInfo
import com.king.dexmorphhunter.model.data.MethodInfo
import com.king.dexmorphhunter.view.adapter.MethodListAdapter
import com.king.dexmorphhunter.view.util.SwipeToDeleteCallback
import com.king.dexmorphhunter.viewmodel.MethodSelectViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@Suppress("NAME_SHADOWING", "SameParameterValue")
@AndroidEntryPoint
class MethodSelectActivity : AppCompatActivity() {

    private val viewModel: MethodSelectViewModel by viewModels()

    @Inject
    lateinit var adapter: MethodListAdapter

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
        binding = ActivityMethodListSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        if (intent != null) {
            packageName = intent.getStringExtra("packageName").toString()
        }

        initViews()
        initViewModel()
        loadClasses()
    }

    private fun initViews() {
        binding.methodSelectListRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        binding.methodSelectListRecyclerView.adapter = adapter

        val swipeToDeleteCallback = SwipeToDeleteCallback(adapter)
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.methodSelectListRecyclerView)

        binding.filterButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    viewModel.setFilterClass(packageName)
                }
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

        binding.addArgumentButton.setOnClickListener {
            updateMethodIsIntercepted(true)
            val method = MethodInfo(
                methodInfo.methodName,
                methodInfo.packageName,
                methodInfo.className,
                isInterceptedMethod = true,
                changeReturnMethod = false,
                methodInfo.methodReturnType,
                methodInfo.methodReturnValue,
                methodInfo.newMethodReturnValue
            )
            adapter.addItem(method)
        }

        binding.classesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                classInfo = classList[position]
                loadMethods()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.methodSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                methodInfo = methodList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.titleTextView.text = packageName
    }

    private fun initViewModel() {
        viewModel.classList.observe(this) { newList ->
            classList = newList ?: emptyList()
            classSpinnerOptions = classList.map { it.className }
            binding.classesSpinner.adapter = ArrayAdapter(
                this,
                R.layout.simple_spinner_dropdown_item,
                classSpinnerOptions
            )
        }

        viewModel.methodList.observe(this) { newList ->
            methodList = newList ?: emptyList()
            methodSpinnerOptions = methodList.map { it.methodName }
            binding.addArgumentButton.isEnabled =
                !methodList.map { it.methodName }.contains("Xposed não encontrado")
            binding.methodSpinner.adapter = ArrayAdapter(
                this,
                R.layout.simple_spinner_dropdown_item,
                methodSpinnerOptions
            )

            for (methodInfo in methodList) {
                if (methodInfo.isInterceptedMethod) {
                    adapter.addItem(methodInfo)
                }
            }
        }
    }

    private fun loadClasses() {
        binding.progressBar.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            packageName.let { viewModel.getClassList(it) }
            withContext(Dispatchers.Main) {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun loadMethods() {
        binding.progressBar.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.getMethodList(classInfo)
            withContext(Dispatchers.Main) {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun updateMethodIsIntercepted(check: Boolean) {
        lifecycleScope.launch {
            viewModel.updateMethodIsIntercepted(methodInfo.className, methodInfo.methodName, check)
        }
    }
}