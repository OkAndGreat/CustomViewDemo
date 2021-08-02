package com.example.customview.test

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.LinearLayout
import com.example.customview.TextChangeView.Other.changed.TuvUtils
import java.lang.Exception

/**
 * Author by OkAndGreat，Date on 2021/8/2.
 *
 */
private const val TAG = "MyLinearLayout"

class MyLinearLayout(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    init {
        //clickable属性一定要设置为true！
        if(!this.isClickable){
            throw Exception("clickable属性一定要设置为true！")
        }
    }

    private val INTERCEPT_VALUE = TuvUtils.dip2px(context, 50F)

    //向上滑动为正值 向下滑动为负值
    private var totalOffsetY = 0F
    private var initialY = 0F

    private var curScrollOffsetY = 0F

    private var curY = 0F
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        curY = ev.y

        if (ev.action == MotionEvent.ACTION_DOWN) {
            initialY = ev.y
        }

        Log.d(TAG, "onTouchEvent: curY --> $curY initialY-->$initialY  action-->${ev.action}")

        if (ev.action == MotionEvent.ACTION_UP) {
            totalOffsetY += (initialY - curY)
            //totalOffsetY的值必须大于等于0,当RecyclerView位于最开始的位置时，如果还向下滑动会导致totalOffsetY为负值
            if (totalOffsetY <= 0) {
                totalOffsetY = 0F
            }
        }

        curScrollOffsetY = TuvUtils.dip2px(context, (initialY - curY) + totalOffsetY).toFloat()
        Log.d(TAG, "InterceptcurScrollOffsetY--> $curScrollOffsetY")

        return curScrollOffsetY < INTERCEPT_VALUE

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        curY = event.y

        if (event.action == MotionEvent.ACTION_DOWN) {
            initialY = event.y
        }

        Log.d(TAG, "onTouchEvent: curY --> $curY initialY-->$initialY  action-->${event.action}")

        if (event.action == MotionEvent.ACTION_UP) {
            totalOffsetY += (initialY - curY)
            //totalOffsetY的值必须大于等于0,当RecyclerView位于最开始的位置时，如果还向下滑动会导致totalOffsetY为负值
            if (totalOffsetY <= 0) {
                totalOffsetY = 0F
            }
        }
        curScrollOffsetY = TuvUtils.dip2px(context, (initialY - curY) + totalOffsetY).toFloat()
        Log.d(TAG, "Touch curScrollOffsetY--> $curScrollOffsetY")
        return super.onTouchEvent(event)
    }
}