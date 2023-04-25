package com.king.dexmorphhunter.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.king.dexmorphhunter.databinding.ItemListMethodBinding
import com.king.dexmorphhunter.model.db.ClassInfo

class MethodListAdapter(
    val context: Context,
    private var ClassAndMethodList: MutableList<ClassInfo>,
    private val onItemClick: Unit,
    private val onDeleteClick: Unit
) : RecyclerView.Adapter<MethodListAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: ItemListMethodBinding,
        private val onItemClick: Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(methodInfo: ClassInfo) {
            binding.classNameTextView.text = methodInfo.className
            //binding.methodNameTextView .text = methodInfo.methodName
            //binding.selectButton.setOnClickListener { onItemClick(methodInfo.methodName) }
            //binding.root.setOnClickListener { onDeleteClick(methodInfo) }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListMethodBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val methodInfo: ClassInfo = ClassAndMethodList[position]
        holder.bind(methodInfo)
    }

    override fun getItemCount(): Int = ClassAndMethodList.size

    fun deleteItem(position: Int) {
        onDeleteClick(ClassAndMethodList[position])
        ClassAndMethodList.removeAt(position)
        notifyItemRemoved(position)
    }

    private fun onDeleteClick(classInfo: ClassInfo) {
        TODO("Not yet implemented")
    }

    fun addItem(classInfo: ClassInfo) {
        ClassAndMethodList.add(classInfo)
        notifyDataSetChanged()
    }

}
