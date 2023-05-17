package com.king.dexmorphhunter.view.util

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.king.dexmorphhunter.view.adapter.MethodListAdapter

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