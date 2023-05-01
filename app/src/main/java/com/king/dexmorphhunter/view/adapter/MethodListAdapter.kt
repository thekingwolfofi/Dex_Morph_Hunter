package com.king.dexmorphhunter.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.king.dexmorphhunter.databinding.ItemListMethodBinding
import com.king.dexmorphhunter.model.util.SwipeToDeleteCallback
import com.king.dexmorphhunter.view.ParameterEditorActivity

class MethodListAdapter(
    val context: Context,
    private var ClassAndMethodList: MutableList<String>
) : RecyclerView.Adapter<MethodListAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: ItemListMethodBinding
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
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val methodInfo: String = ClassAndMethodList[position]
        holder.bind(methodInfo)
    }

    override fun getItemCount(): Int = ClassAndMethodList.size

    fun deleteItem(position: Int) {
        onDeleteClick(ClassAndMethodList[position])
        notifyItemRemoved(position)
         // Atualiza as posições dos itens na lista
    }

    private fun onDeleteClick(classInfo: String) {
        val position = ClassAndMethodList.indexOf(classInfo)
        if (position != -1) {
            ClassAndMethodList.removeAt(position)
            updateItemPositions()
            notifyItemRemoved(position)
        }
    }

    private fun updateItemPositions() {
        for (i in 0 until ClassAndMethodList.size) {
            notifyItemChanged(i)
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun addItem(classInfo: String) {
        ClassAndMethodList.add(classInfo)
        notifyDataSetChanged()
    }


    fun enableSwipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = SwipeToDeleteCallback(this)
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

}
