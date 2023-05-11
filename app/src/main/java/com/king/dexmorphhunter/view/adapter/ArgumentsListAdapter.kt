package com.king.dexmorphhunter.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.king.dexmorphhunter.databinding.ItemParameterListBinding
import com.king.dexmorphhunter.model.data.ArgumentInfo
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ArgumentsListAdapter @Inject constructor(
        private val argumentList: List<ArgumentInfo>
    ) : RecyclerView.Adapter<ArgumentsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemParameterListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val argument = argumentList[position]
        holder.bind(argument)
    }

    override fun getItemCount() = argumentList.size

    class ViewHolder(private val binding: ItemParameterListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(parameter: ArgumentInfo) {
            binding.parameterName.hint = parameter.name
            binding.parameterValueListButton.visibility = View.GONE
            binding.parameterTextValue.visibility = View.GONE
            binding.parameterTextNewValue.visibility = View.GONE
            binding.parameterNumericValue.visibility = View.GONE
            binding.parameterNumericNewValue.visibility = View.GONE
            binding.parameterValueBoolean.visibility = View.GONE
            binding.parameterNewValueBoolean.visibility = View.GONE

            when (parameter.type) {
                String::class.java -> {
                    val value = parameter.value as? String
                    binding.parameterTextValue.setText(value)
                    binding.parameterTextValue.visibility = View.VISIBLE
                    binding.parameterTextNewValue.visibility = View.VISIBLE
                    binding.parameterTextNewValue.setText(value)
                }
                Int::class.java, Long::class.java, Float::class.java, Double::class.java -> {
                    val value = parameter.value?.toString()
                    binding.parameterNumericValue.setText(value)
                    binding.parameterNumericValue.visibility = View.VISIBLE
                    binding.parameterNumericNewValue.visibility = View.VISIBLE
                    binding.parameterNumericNewValue.setText(value)
                }
                Boolean::class.java -> {
                    val value = parameter.value as? Boolean
                    binding.parameterValueBoolean.isChecked = value ?: false
                    binding.parameterValueBoolean.visibility = View.VISIBLE
                    binding.parameterNewValueBoolean.visibility = View.VISIBLE
                    binding.parameterNewValueBoolean.isChecked = value ?: false
                }
                List::class.java -> {
                    binding.parameterValueListButton.visibility = View.VISIBLE
                    binding.parameterValueListButton.setOnClickListener {
                        // Lógica para lidar com o tipo List aqui
                    }
                }
                Array::class.java -> {
                    binding.parameterValueListButton.visibility = View.VISIBLE
                    binding.parameterValueListButton.setOnClickListener {
                        // Lógica para lidar com o tipo Array aqui
                    }
                }
                else -> {
                binding.parameterValueListButton.visibility = View.VISIBLE
                binding.parameterValueListButton.setOnClickListener {
                    // Lógica para lidar com o tipo de objeto customizado aqui
                }
            }

            }
        }
    }
}
