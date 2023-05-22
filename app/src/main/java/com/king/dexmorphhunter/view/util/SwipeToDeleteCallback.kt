package com.king.dexmorphhunter.view.util

import android.graphics.Canvas
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

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        // Limitar a distância máxima do swipe
        val itemView = viewHolder.itemView
        val itemWidth = itemView.width.toFloat()
        val maxSwipeDistance = itemWidth * 0.15f // Ajuste o valor conforme necessário

        // Restringir o valor de dX para limitar a distância do swipe
        val limitedDX = when {
            dX < -maxSwipeDistance -> -maxSwipeDistance
            dX > maxSwipeDistance -> maxSwipeDistance
            else -> dX
        }

        super.onChildDraw(c, recyclerView, viewHolder, limitedDX, dY, actionState, isCurrentlyActive)
    }

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

        if (!adapter.isDeleteConfirmationVisible) {
            adapter.isDeleteConfirmationVisible =
                !(direction == ItemTouchHelper.LEFT || direction == ItemTouchHelper.RIGHT)
        } else {
            adapter.isDeleteConfirmationVisible = false
        }

        // Notifica o adapter para atualizar a aparência dos itens
        adapter.notifyItemChanged(position)
    }

}