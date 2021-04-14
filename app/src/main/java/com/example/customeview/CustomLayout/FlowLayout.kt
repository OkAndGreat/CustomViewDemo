package com.example.customeview.CustomLayout

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.children
import kotlin.math.max


class FlowLayout(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {
    private val childrenBounds = mutableListOf<Rect>()
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val sizeWidth = MeasureSpec.getSize(widthMeasureSpec)
        val sizeHeight = MeasureSpec.getSize(heightMeasureSpec)
        val modeWidth = MeasureSpec.getMode(widthMeasureSpec)
        val modeHeight = MeasureSpec.getMode(heightMeasureSpec)

        var maxLineWidth = 0
        var maxLineHeight = 0

        var currentLineWidth = 0
        var currentLineHeight = 0


        for ((index, child) in children.withIndex()) {

            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            //考虑到onMeasure可能调用多次，因此加个if判断语句
            if(childrenBounds.size<index+1) childrenBounds.add(Rect())

            val layoutParams = child.layoutParams as MarginLayoutParams
            val measuredWidth = child.measuredWidth
            val measuredHeight = child.measuredHeight

            //暂时未考虑一行文字过长导致一行装不下的问题
            if(currentLineWidth+layoutParams.leftMargin+layoutParams.rightMargin+child.measuredWidth>sizeWidth){
                currentLineWidth=0
                childrenBounds[index].left=currentLineWidth+layoutParams.leftMargin
                childrenBounds[index].right=currentLineWidth+measuredWidth+layoutParams.leftMargin
                currentLineWidth=layoutParams.leftMargin+layoutParams.rightMargin+child.measuredWidth
                maxLineWidth= max(maxLineWidth,currentLineWidth)

                currentLineHeight+=maxLineHeight
                maxLineHeight=0
                childrenBounds[index].top=currentLineHeight+layoutParams.topMargin
                childrenBounds[index].bottom=currentLineHeight+layoutParams.topMargin+measuredHeight
                maxLineHeight=max(maxLineHeight,layoutParams.bottomMargin+layoutParams.topMargin+measuredHeight)
            }else{
                childrenBounds[index].left=currentLineWidth+layoutParams.leftMargin
                childrenBounds[index].right=currentLineWidth+measuredWidth+layoutParams.leftMargin
                currentLineWidth+=layoutParams.leftMargin+layoutParams.rightMargin+child.measuredWidth
                maxLineWidth= max(maxLineWidth,currentLineWidth)

                childrenBounds[index].top=currentLineHeight+layoutParams.topMargin
                childrenBounds[index].bottom=currentLineHeight+layoutParams.topMargin+measuredHeight
                maxLineHeight=max(maxLineHeight,layoutParams.bottomMargin+layoutParams.topMargin+measuredHeight)
            }
        }

        val height = if (modeHeight == MeasureSpec.EXACTLY) sizeHeight else maxLineHeight
        val width = if (modeWidth == MeasureSpec.EXACTLY) sizeWidth else maxLineWidth
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for ((index, child) in children.withIndex()) {
            child.layout(childrenBounds[index].left, childrenBounds[index].top, childrenBounds[index].right, childrenBounds[index].bottom)
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }
}