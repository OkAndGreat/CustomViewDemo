package com.example.customview.window


import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "CustomItemTouchHelper"
class CustomItemTouchHelper : ItemTouchHelper.SimpleCallback(
    0,
    15
) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        Log.d(TAG, "onSwiped: ")
    }


    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder) = 0.2f
}