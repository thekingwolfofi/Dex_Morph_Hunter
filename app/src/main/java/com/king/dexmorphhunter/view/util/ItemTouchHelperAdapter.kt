package com.king.dexmorphhunter.view.util

interface ItemTouchHelperAdapter {
    fun onItemDismiss(position: Int)
    fun onItemMove(fromPosition: Int, toPosition: Int)
}