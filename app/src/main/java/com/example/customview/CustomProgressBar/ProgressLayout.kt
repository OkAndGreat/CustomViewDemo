package com.example.customview.CustomProgressBar

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import com.example.customview.TextChangeView.Other.changed.CountView

class ProgressLayout(context: Context?, attrs: AttributeSet?) :
    LinearLayout(context, attrs) {

    private lateinit var mProgressView: ProgressView
    private lateinit var mTextView: CountView

    private var maxCount = 5
    private var curCount = 0

    //TODO:用typedArray初始化属性
    init {

        init()
    }

    private fun init() {
        removeAllViews()
        orientation = VERTICAL

        addTextView()
        mTextView.count = 0
        addProgressView()
    }

    private fun addProgressView() {
        mProgressView = ProgressView(context)
        addView(mProgressView, getProgressViewParams())
    }

    //params.xxxMargin = paddingxxx 是为了处理 ProgressLayout设置padding时它内部控件的摆放情况
    private fun getProgressViewParams(): LayoutParams {
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        params.topMargin = paddingTop
        params.leftMargin = paddingLeft
        params.rightMargin = paddingRight
        return params
    }

    private fun addTextView() {
        mTextView = CountView(context)
        addView(mTextView, getTextViewParams())
    }

    private fun getTextViewParams(): LayoutParams {
        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        params.leftMargin = paddingLeft
        params.rightMargin = paddingRight
        //这里加20的目的是为了让TextView和进度条View有一定间隔
        params.bottomMargin = paddingBottom + 20
        params.gravity = Gravity.CENTER
        return params
    }


    fun setCurCount() {
        mProgressView.setCurCount(5)
        mTextView.calculateChangeNum(9)
    }

}