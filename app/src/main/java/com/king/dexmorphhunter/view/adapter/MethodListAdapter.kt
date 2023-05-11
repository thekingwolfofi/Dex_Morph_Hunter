package com.king.dexmorphhunter.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.king.dexmorphhunter.databinding.ItemListMethodBinding
import com.king.dexmorphhunter.view.ParameterEditorActivity
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class MethodListAdapter @Inject constructor(
    @ApplicationContext val context: Context,
    private var ClassAndMethodList: MutableList<String>
) : RecyclerView.Adapter<MethodListAdapter.ViewHolder>() {

    var swipedPosition = -1
    var deleteConfirmationPosition = -1
    var isDeleteConfirmationVisible = false

    inner class ViewHolder(
        private val binding: ItemListMethodBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(methodInfo: String) {
            binding.classNameTextView.text = methodInfo

            if (isDeleteConfirmationVisible && adapterPosition == deleteConfirmationPosition) {
                // Exibe o botão de exclusão e oculta o ícone de exclusão padrão
                    binding.deleteIcon.visibility = View.VISIBLE

                // Define o clique no botão de exclusão para confirmar a exclusão
                binding.deleteIcon.setOnClickListener {
                    val position = adapterPosition
                    deleteItem(position)
                }
            } else {
                // Oculta o botão de exclusão e exibe o ícone de exclusão padrão
                binding.deleteIcon.visibility = View.GONE
            }

            binding.selectButton.setOnClickListener {
                val intent = Intent(context, ParameterEditorActivity::class.java)
                context.startActivity(intent)
            }
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
        ClassAndMethodList.removeAt(position)
        notifyItemRemoved(position)

        // Redefine as variáveis de confirmação para ocultar o botão de exclusão
        swipedPosition = -1
        deleteConfirmationPosition = -1
        isDeleteConfirmationVisible = false
        updateItemPositions()
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

@Suppress("DEPRECATION")
class SwipeToDeleteCallback(
    private val adapter: MethodListAdapter
) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN,
    ItemTouchHelper.START or ItemTouchHelper.END
) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition

        // Define as posições do item deslizado e do item de confirmação
        adapter.swipedPosition = position
        adapter.deleteConfirmationPosition = position

        adapter.isDeleteConfirmationVisible =
            !(direction == ItemTouchHelper.LEFT || direction == ItemTouchHelper.RIGHT)

        // Notifica o adapter para atualizar a aparência dos itens
        adapter.notifyItemChanged(position)
    }


}