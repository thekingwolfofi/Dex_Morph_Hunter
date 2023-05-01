package com.king.dexmorphhunter.view

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.king.dexmorphhunter.databinding.ActivityMethodListSelectBinding
import com.king.dexmorphhunter.model.xposed.MethodExtractorXposedModule
import com.king.dexmorphhunter.model.util.PackageUtils
import com.king.dexmorphhunter.view.adapter.MethodListAdapter

@Suppress("NAME_SHADOWING")
class MethodSelectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMethodListSelectBinding
    //private lateinit var viewModel: MethodSelectViewModel
    private lateinit var adapter: MethodListAdapter
    private lateinit var packageUtils: PackageUtils
    private lateinit var methodExtractorXposedModule: MethodExtractorXposedModule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializa o contexto
        val context = applicationContext

        // Inicialize a propriedade packageUtils aqui
        packageUtils = PackageUtils


        // Inicialize a propriedade packageUtils aqui
        methodExtractorXposedModule = MethodExtractorXposedModule()

        // Inicializa as variaveis passadas por instancia
        val packageName = intent.getStringExtra("packageName")

        // Inializa o filtro e seta a lista como filtrada
        var isFilterEnabled = true
        val classList = packageUtils.getListClassesInPackage(context, packageName.toString())

        // Atualiza a lista de classes de acordo com o filtro
        val filteredClassList: List< String> = if (isFilterEnabled) {
            classList.filter { !it.contains("$") }
        } else {
            classList
        }

        // Inicializa a view binding
        binding = ActivityMethodListSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.titleTextView.text = packageName

        val adapterClassSpinner = arrayAdapter(filteredClassList)

        // Define o layout a ser usado para exibir cada item do spinner
        adapterClassSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Define o adaptador para o spinner de classes
        binding.classesSpinner.adapter = adapterClassSpinner


        // Obter o texto selecionado
        //val selectedClass = binding.classesSpinner.adapter.getItem(1).toString()

        // Obter todos os nomes de métodos da classe selecionada
        //val methodList = methodExtractorXposedModule.getAllMethodNames(selectedClass)


        // Inicialize o adapter
        adapter = MethodListAdapter(this, mutableListOf())

        // Configure o RecyclerView
        binding.methodSelectListRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.methodSelectListRecyclerView.adapter = adapter

        adapter.enableSwipeToDelete(binding.methodSelectListRecyclerView)

        // Adiciona um novo objeto sempre que o botão "add" for clicado
        binding.addArgumentButton.setOnClickListener {
            val classInfo = "teste"
            adapter.addItem(classInfo)
        }

        // Adiciona o listener para o botão de filtro
        binding.filterButton.setOnClickListener {
            val classList = packageUtils.getListClassesInPackage(context, packageName.toString())
            isFilterEnabled = !isFilterEnabled

            // Atualiza a lista de classes de acordo com o filtro
            val filteredClassList: List< String> = if (isFilterEnabled) {
                classList.filter { !it.contains("$") }
            } else {
                classList
            }

            // Atualiza o adapter do spinner
            adapterClassSpinner.clear()
            adapterClassSpinner.addAll(filteredClassList)
            adapterClassSpinner.notifyDataSetChanged()
        }
    }

    private fun arrayAdapter(list: List<String>): ArrayAdapter<String> {
       return ArrayAdapter(this, android.R.layout.simple_spinner_item, list)
    }

}