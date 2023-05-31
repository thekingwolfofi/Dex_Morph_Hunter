package com.king.dexmorphhunter.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.king.dexmorphhunter.App
import com.king.dexmorphhunter.databinding.ItemListMethodBinding
import com.king.dexmorphhunter.model.data.MethodInfo
import com.king.dexmorphhunter.view.ParameterEditorActivity
import com.king.dexmorphhunter.view.util.ItemTouchHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("DEPRECATION")
@Singleton
class MethodListAdapter @Inject constructor() : RecyclerView.Adapter<MethodListAdapter.MethodSelectViewHolder>(), ItemTouchHelper {

    private val context: Context = App.instance.applicationContext
    var swipedPosition: Int = -1
    var deleteConfirmationPosition: Int = -1
    var isDeleteConfirmationVisible: Boolean = false

    private var itemList: MutableList<MethodInfo> = mutableListOf()

    fun addItem(methodInfo: MethodInfo) {
        val methodName = methodInfo.methodName

        if (!itemList.any { it.methodName == methodName }) {
            itemList.add(methodInfo)
            notifyItemInserted(itemList.size - 1)

            // Verificar se o item adicionado é igual ao item em estado de exclusão
            if (deleteConfirmationPosition != -1 && itemList[deleteConfirmationPosition] == methodInfo) {
                deleteConfirmationPosition = -1
                isDeleteConfirmationVisible = false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MethodSelectViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemListMethodBinding.inflate(inflater, parent, false)
        return MethodSelectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MethodSelectViewHolder, position: Int) {
        holder.bind(itemList[position])

        // Verificar se a confirmação de exclusão deve ser exibida ou ocultada
        if (position == deleteConfirmationPosition && isDeleteConfirmationVisible) {
            // Mostrar confirmação de exclusão
            holder.showDeleteConfirmation()
        } else {
            // Ocultar confirmação de exclusão
            holder.hideDeleteConfirmation()
        }

    }


    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class MethodSelectViewHolder(private val itemBinding: ItemListMethodBinding) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(methodInfo: MethodInfo) {
            itemBinding.classNameTextView.text = methodInfo.className
            itemBinding.methodNameTextView.text = methodInfo.methodName
            itemBinding.selectButton.setOnClickListener {

                val intent = Intent(context, ParameterEditorActivity::class.java)
                intent.putExtra("className", methodInfo.className)
                intent.putExtra("methodName", methodInfo.methodName)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)

            }

        }

        fun showDeleteConfirmation() {
            // Exibir a confirmação de exclusão

            itemBinding.deleteIcon.visibility = View.VISIBLE

            itemBinding.deleteIcon.setOnClickListener {
                // Remover o item quando a confirmação de exclusão for clicada
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    removeItem(position)
                }
            }
        }

        fun hideDeleteConfirmation() {
            // Ocultar a confirmação de exclusão
            itemBinding.deleteIcon.visibility = View.GONE
        }

        private fun removeItem(position: Int) {
            itemList.removeAt(position)
            notifyItemRemoved(position)
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
