package com.example.customview.window

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "CustomLayoutManger"
class CustomLayoutManger : RecyclerView.LayoutManager() {
    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams =
        RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        Log.d(TAG, "onLayoutChildren: ")
        // 回收 复用
        detachAndScrapAttachedViews(recycler)
        val itemCount = itemCount
        for (i in 0 until itemCount step 1) {
            Log.d(TAG, "onLayoutChildren: i-->$i itemCound-->$itemCount")
            //复用
            val view = recycler.getViewForPosition(i)
            addView(view)

            measureChildWithMargins(view, 0, 0)

            // 求padding的总大小
            val widthSpace = width - getDecoratedMeasuredWidth(view)
            val heightSpace = height - getDecoratedMeasuredHeight(view)

            // 布局
            layoutDecoratedWithMargins(
                view,
                widthSpace / 2,
                heightSpace / 2,
                widthSpace / 2 + getDecoratedMeasuredWidth(view),
                heightSpace / 2 + getDecoratedMeasuredHeight(view)
            )
        }
    }
}