package com.example.customeview.CustomViewDrag

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import java.util.ArrayList

private const val COLUMNS = 4
private const val ROWS = 6
private const val TAG = "MyDragView"

class MyDragView(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {
    private var dragListener: OnDragListener = DragListener()
    private var draggedView: View? = null
    private var orderedChildren: MutableList<View> = ArrayList()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val specWidth = MeasureSpec.getSize(widthMeasureSpec)
        val specHeight = MeasureSpec.getSize(heightMeasureSpec)
        val childWidth = specWidth / COLUMNS
        val childHeight = specHeight / ROWS
        measureChildren(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY))
        setMeasuredDimension(specWidth, specHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var childLeft: Int
        var childTop: Int
        val childWidth = width / COLUMNS
        val childHeight = height / ROWS
        for ((index, child) in children.withIndex()) {
            childLeft = index % 4 * childWidth
            childTop = index / 4 * childHeight
            child.layout(0, 0, childWidth, childHeight)
            child.translationX = childLeft.toFloat()
            child.translationY = childTop.toFloat()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        for (child in children) {
            orderedChildren.add(child) // 初始化位置
            child.setOnLongClickListener { v ->
                draggedView = v
                Log.d(TAG, "onFinishInflate: draggedView被赋值了")
                v.startDrag(null, DragShadowBuilder(v), v, 0)
                false
            }
            child.setOnDragListener(dragListener)
        }
    }

    private inner class DragListener : OnDragListener {
        override fun onDrag(v: View, event: DragEvent): Boolean {
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> if (event.localState === v) {
                    v.visibility = View.INVISIBLE
                }
                DragEvent.ACTION_DRAG_ENTERED -> if (event.localState !== v) {
                    sort(v)
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                }
                DragEvent.ACTION_DRAG_ENDED -> if (event.localState === v) {
                    v.visibility = View.VISIBLE
                }
            }
            return true
        }
    }

    override fun onDragEvent(event: DragEvent?): Boolean {
        return super.onDragEvent(event)

    }

    private fun sort(targetView: View) {
        var draggedIndex = -1
        var targetIndex = -1
        for ((index, child) in orderedChildren.withIndex()) {
            if (targetView === child) {
                targetIndex = index
            } else if (draggedView === child) {
                draggedIndex = index
            }
        }
        Log.d(TAG, "sort: draggedIndex-->$draggedIndex  targetIndex-->$targetIndex")
        val draggedLeft: Int
        val draggedTop: Int
        val targetLeft: Int
        val targetTop: Int
        val childWidth = width / COLUMNS
        val childHeight = height / ROWS


        draggedLeft = draggedIndex % 4 * childWidth;targetLeft = targetIndex % 4 * childWidth
        draggedTop = draggedIndex / 4 * childHeight;targetTop = targetIndex / 4 * childHeight

        orderedChildren[draggedIndex].animate()
                .translationX(draggedLeft.toFloat())
                .translationY(draggedTop.toFloat()).duration = 150

        orderedChildren[targetIndex].animate()
                .translationX(targetLeft.toFloat())
                .translationY(targetTop.toFloat()).duration = 150

        orderedChildren.removeAt(draggedIndex)
        orderedChildren.add(targetIndex, draggedView!!)
    }

}