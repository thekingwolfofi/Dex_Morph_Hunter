package com.king.dexmorphhunter.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.king.dexmorphhunter.databinding.ItemListMethodBinding
import com.king.dexmorphhunter.view.ParameterEditorActivity

class MethodListAdapter(
    val context: Context,
    private var ClassAndMethodList: MutableList<String>,
    private val onItemClick: Unit,
    private val onDeleteClick: Unit
) : RecyclerView.Adapter<MethodListAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: ItemListMethodBinding,
        private val onItemClick: Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(methodInfo: String) {
            binding.classNameTextView.text = methodInfo
            //binding.methodNameTextView .text = methodInfo.methodName
            binding.selectButton.setOnClickListener {
                val intent = Intent(context, ParameterEditorActivity::class.java)
                context.startActivity(intent)
            }
            //binding.root.setOnClickListener { onDeleteClick(methodInfo) }

        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListMethodBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val methodInfo: String = ClassAndMethodList[position]
        holder.bind(methodInfo)
    }

    override fun getItemCount(): Int = ClassAndMethodList.size

    fun deleteItem(position: Int) {
        onDeleteClick(ClassAndMethodList[position])
        ClassAndMethodList.removeAt(position)
        notifyItemRemoved(position)
    }

    private fun onDeleteClick(classInfo: String) {
        TODO("Not yet implemented")
    }

    fun addItem(classInfo: String) {
        ClassAndMethodList.add(classInfo)
        notifyDataSetChanged()
    }

}
