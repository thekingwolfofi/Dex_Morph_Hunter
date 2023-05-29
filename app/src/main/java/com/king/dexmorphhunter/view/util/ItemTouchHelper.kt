package com.king.dexmorphhunter.view.util

interface ItemTouchHelper {

    fun onItemDismiss(position: Int)
    fun onItemMove(fromPosition: Int, toPosition: Int)
}