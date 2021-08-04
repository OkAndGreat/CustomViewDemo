package com.example.customview.test

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView

/**
 * Author by OkAndGreat，Date on 2021/8/2.
 * 这个Layout里面的RecyclerView的LayoutManger强制使用MyLayoutManger
 * 开启clickable属性可以让在不可滑动的地方滑动时也能接收到滑动事件 方便我们更好的记录手指移动的Y值
 */
private const val TAG = "MyLinearLayout"

class CustomLinearLayout(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mLayoutManger: MyLayoutManger
    private lateinit var mConstraintLayout: ConstraintLayout

    //将对应的View进行保存
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.d(TAG, "onMeasure: childcount-->$childCount")
        mConstraintLayout = getChildAt(0) as ConstraintLayout
        for (child in children) {
            Log.d(TAG, "onMeasure: ${child is ViewGroup}")
            if (child is RecyclerView) {
                mRecyclerView = child
                mLayoutManger = mRecyclerView.layoutManager as MyLayoutManger
            }
        }
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        Log.d(TAG, "dispatchTouchEvent: ${ev.x}")
        return super.dispatchTouchEvent(ev)
    }


}