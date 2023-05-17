package com.king.dexmorphhunter.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.king.dexmorphhunter.databinding.ItemParameterListBinding
import com.king.dexmorphhunter.model.data.ArgumentInfo
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

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

        fun bind(argumentInfo: ArgumentInfo) {
            binding.argumentName.hint = argumentInfo.argumentName
            binding.argumentValueListButton.visibility = View.GONE
            binding.argumentTextValue.visibility = View.GONE
            binding.argumentTextNewValue.visibility = View.GONE
            binding.argumentNumericValue.visibility = View.GONE
            binding.argumentNumericNewValue.visibility = View.GONE
            binding.argumentValueBoolean.visibility = View.GONE
            binding.argumentNewValueBoolean.visibility = View.GONE

            when (argumentInfo.argumentType) {
                String::class.java -> {
                    val value = argumentInfo.argumentValue as? String
                    binding.argumentTextValue.setText(value)
                    binding.argumentTextValue.visibility = View.VISIBLE
                    binding.argumentTextNewValue.visibility = View.VISIBLE
                    binding.argumentTextNewValue.setText(value)
                }
                Int::class.java, Long::class.java, Float::class.java, Double::class.java -> {
                    val value = argumentInfo.argumentValue?.toString()
                    binding.argumentNumericValue.setText(value)
                    binding.argumentNumericValue.visibility = View.VISIBLE
                    binding.argumentNumericNewValue.visibility = View.VISIBLE
                    binding.argumentNumericNewValue.setText(value)
                }
                Boolean::class.java -> {
                    val argumentValue = argumentInfo.argumentValue as? Boolean
                    binding.argumentValueBoolean.isChecked = argumentValue ?: false
                    binding.argumentValueBoolean.visibility = View.VISIBLE
                    binding.argumentNewValueBoolean.visibility = View.VISIBLE
                    binding.argumentNewValueBoolean.isChecked = argumentValue ?: false
                }
                List::class.java -> {
                    binding.argumentValueListButton.visibility = View.VISIBLE
                    binding.argumentValueListButton.setOnClickListener {
                        // Lógica para lidar com o tipo List aqui
                    }
                }
                Array::class.java -> {
                    binding.argumentValueListButton.visibility = View.VISIBLE
                    binding.argumentValueListButton.setOnClickListener {
                        // Lógica para lidar com o tipo Array aqui
                    }
                }
                else -> {
                binding.argumentValueListButton.visibility = View.VISIBLE
                binding.argumentValueListButton.setOnClickListener {
                    // Lógica para lidar com o tipo de objeto customizado aqui
                }
            }

            }
        }
    }
}
