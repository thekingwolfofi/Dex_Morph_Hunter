package com.king.dexmorphhunter.model.util

import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.king.dexmorphhunter.view.adapter.MethodListAdapter

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
        // Não é necessário implementar essa função, pois não vamos permitir
        // que os itens sejam movidos na lista
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // Quando um item for deslizado, vamos removê-lo do adapter
        val position = viewHolder.adapterPosition
        adapter.deleteItem(position)
    }

     override fun getSwipeDirs(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        // Vamos permitir que o usuário deslize o item para o lado esquerdo ou direito
        // para remover o item da lista
        return super.getSwipeDirs(recyclerView, viewHolder)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        // Adiciona o botão de excluir quando o item é deslizado
        val itemView = viewHolder.itemView
        val icon = ContextCompat.getDrawable(adapter.context, android.R.drawable.ic_menu_delete)
        val iconMargin = (itemView.height - icon!!.intrinsicHeight) / 2
        val iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
        val iconBottom = iconTop + icon.intrinsicHeight

        if (dX > 0) {
            // Deslizando para a direita
            val iconLeft = itemView.left + iconMargin
            val iconRight = itemView.left + iconMargin + icon.intrinsicWidth

            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
        } else if (dX < 0) {
            // Deslizando para a esquerda
            val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
            val iconRight = itemView.right - iconMargin

            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
        }

        icon.draw(c)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}
