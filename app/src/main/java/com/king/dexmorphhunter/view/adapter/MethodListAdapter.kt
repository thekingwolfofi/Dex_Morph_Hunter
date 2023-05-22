package com.king.dexmorphhunter.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.king.dexmorphhunter.databinding.ItemListMethodBinding
import com.king.dexmorphhunter.model.data.MethodInfo
import com.king.dexmorphhunter.view.util.ItemTouchHelperAdapter
import javax.inject.Inject


class MethodListAdapter @Inject constructor() : RecyclerView.Adapter<MethodListAdapter.MethodSelectViewHolder>(), ItemTouchHelperAdapter {

    var swipedPosition: Int = -1
    var deleteConfirmationPosition: Int = -1
    var isDeleteConfirmationVisible: Boolean = false

    private var itemList: MutableList<MethodInfo> = mutableListOf()

    fun addItem(methodInfo: MethodInfo) {
        val methodName = methodInfo.methodName

        if (!itemList.any { it.methodName == methodName }) {
            itemList.add(methodInfo)
            notifyItemInserted(itemList.size - 1)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MethodSelectViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemListMethodBinding.inflate(inflater, parent, false)
        return MethodSelectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MethodSelectViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class MethodSelectViewHolder(private val itemBinding: ItemListMethodBinding) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(methodInfo: MethodInfo ) {
            itemBinding.classNameTextView.text = methodInfo.className
            itemBinding.methodNameTextView.text = methodInfo.methodName
            itemBinding.selectButton.setOnClickListener {

            }
        }
    }

    override fun onItemDismiss(position: Int) {
        swipedPosition = position
        deleteConfirmationPosition = position
        isDeleteConfirmationVisible = true
        notifyItemChanged(position)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        // não precisa ser implementado
    }
}

