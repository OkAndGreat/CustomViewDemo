package com.example.customview.TextChangeView.mine

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.customview.R
import com.example.customview.TextChangeView.Other.changed.CountView
import com.example.customview.TextChangeView.Other.changed.TuvUtils

class MyThumbUpView(context: Context?, attrs: AttributeSet?) :
    LinearLayout(context, attrs), View.OnClickListener {
    val DEFAULT_DRAWABLE_PADDING = 4f
    private lateinit var mThumbView: MyThumbView
    private lateinit var mCountView: CountView

    private var mDrawablePadding = 0f
    private var mTextColor = 0
    private var mCount = 0
    private var mTextSize = 0f
    private var mIsThumbUp = false
    private var mTopMargin = 0
    private var mNeedChangeChildView = false


    init {
        val typedArray = context!!.obtainStyledAttributes(attrs, R.styleable.ThumbUpView)
        mDrawablePadding = typedArray.getDimension(
            R.styleable.ThumbUpView_tuv_drawable_padding,
            TuvUtils.dip2px(context, DEFAULT_DRAWABLE_PADDING).toFloat()
        )
        mCount = typedArray.getInt(R.styleable.ThumbUpView_tuv_count, 0)
        mTextColor = typedArray.getColor(
            R.styleable.ThumbUpView_tuv_text_color,
            Color.parseColor(CountView.DEFAULT_TEXT_COLOR)
        )
        mTextSize = typedArray.getDimension(
            R.styleable.ThumbUpView_tuv_drawable_padding,
            TuvUtils.sp2px(context, CountView.DEFAULT_TEXT_SIZE).toFloat()
        )
        mIsThumbUp = typedArray.getBoolean(R.styleable.ThumbUpView_tuv_isThumbUp, false)
        typedArray.recycle()

        init()
    }

    private fun init() {
        removeAllViews()
        clipChildren = false
        orientation = HORIZONTAL

        addThumbView()

        addCountView()

        //把设置的padding分解到子view，否则对超出view范围的动画显示不全

        //把设置的padding分解到子view，否则对超出view范围的动画显示不全
        setPadding(0, 0, 0, 0, false)
        setOnClickListener(this)
    }

    private fun addCountView() {
        mCountView = CountView(context)
        mCountView.setTextColor(mTextColor)
        mCountView.setTextSize(mTextSize)
        mCountView.count = mCount

        addView(mCountView, getCountParams())
    }

    fun setPadding(left: Int, top: Int, right: Int, bottom: Int, needChange: Boolean) {
        mNeedChangeChildView = needChange
        setPadding(left, top, right, bottom)
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        if (mNeedChangeChildView) {
            resetThumbParams()
            resetCountViewParams()
            mNeedChangeChildView = false
        } else {
            super.setPadding(left, top, right, bottom)
        }
    }

    private fun resetThumbParams() {
        val params = mThumbView.layoutParams as LayoutParams
        if (mTopMargin < 0) {
            params.topMargin = mTopMargin //设置这个距离是为了文字与拇指居中显示
        }
        params.leftMargin = paddingLeft
        params.topMargin += paddingTop
        params.bottomMargin = paddingBottom
        mThumbView.layoutParams = params
    }

    private fun resetCountViewParams() {
        val params = mCountView.layoutParams as LayoutParams
        if (mTopMargin > 0) {
            params.topMargin = mTopMargin //设置这个距离是为了文字与拇指居中显示
        }
        params.leftMargin = mDrawablePadding.toInt()
        params.topMargin += paddingTop
        params.bottomMargin = paddingBottom
        params.rightMargin = paddingRight
        mCountView.layoutParams = params
    }

    fun setCount(mCount: Int): MyThumbUpView {
        this.mCount = mCount
        mCountView.count = mCount
        return this
    }

    fun setTextColor(mTextColor: Int): MyThumbUpView {
        this.mTextColor = mTextColor
        mCountView.setTextColor(mCount)
        return this
    }

    fun setTextSize(mTextSize: Float): MyThumbUpView {
        this.mTextSize = mTextSize
        mCountView.setTextSize(mCount.toFloat())
        return this
    }

    fun setThumbUp(isThumbUp: Boolean): MyThumbUpView {
        mIsThumbUp = isThumbUp
        mThumbView.setIsThumbUp(mIsThumbUp)
        return this
    }

    fun setThumbUpClickListener(listener: MyThumbView.ThumbUpClickListener) {
        mThumbView.setThumbUpClickListener(listener)
    }

    private fun addThumbView() {
        mThumbView = MyThumbView(context)
        mThumbView.setIsThumbUp(mIsThumbUp)
        val circlePoint = mThumbView.getCirclePoint()
        mTopMargin = (circlePoint.y - mTextSize / 2).toInt()
        addView(mThumbView, getThumbParams())
    }

    private fun getThumbParams(): LayoutParams? {
        val params =
            LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        if (mTopMargin < 0) {
            params.topMargin = mTopMargin //设置这个距离是为了文字与拇指居中显示
        }
        params.leftMargin = paddingLeft
        params.topMargin += paddingTop
        params.bottomMargin = paddingBottom
        return params
    }

    private fun getCountParams(): LayoutParams? {
        val params =
            LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        if (mTopMargin > 0) {
            params.topMargin = mTopMargin //设置这个距离是为了文字与拇指居中显示
        }
        params.leftMargin = mDrawablePadding.toInt()
        params.topMargin += paddingTop
        params.bottomMargin = paddingBottom
        params.rightMargin = paddingRight
        return params
    }

    override fun onClick(v: View?) {
        if (!mThumbView.isOnAnimation()) {
            mIsThumbUp = !mIsThumbUp
            if (mIsThumbUp) {
                mCountView.calculateChangeNum(1)
            } else {
                mCountView.calculateChangeNum(-1)
            }
            mThumbView.startAnim()
        }
    }
}