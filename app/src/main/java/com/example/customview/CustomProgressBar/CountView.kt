package com.example.customview.CustomProgressBar

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import com.example.customview.TextChangeView.Other.changed.TuvPoint
import com.example.customview.TextChangeView.Other.changed.TuvUtils
import kotlin.math.abs
import kotlin.math.ceil

class CountView(context: Context) : View(context) {
    companion object{
        const val DEFAULT_TEXT_COLOR = "#cccccc"
        const val DEFAULT_TEXT_SIZE = 45f
        const val COUNT_ANIM_DURING = 5000
    }

    private var mTextPaint:Paint
    private var mTextSize = DEFAULT_TEXT_SIZE
    private var mTextColor = 0
    private var mEndTextColor = 0

    private var mCount = 0
    private var mMaxCount = 0
    //mTexts[0]是不变的部分，mTexts[1]原来的部分，mTexts[2]变化后的部分
    private var mTexts: Array<String?> = arrayOfNulls(3)

    //表示各部分的坐标
    private var mTextPoints
            : Array<TuvPoint> = arrayOf(TuvPoint(), TuvPoint(),TuvPoint(),TuvPoint())

    private var mMaxOffsetY = 0f
    private var mMinOffsetY = 0f

    private var mOldOffsetY = 0f
    private var mNewOffsetY = 0f
    private var mFraction = 0f

    private var mCarryNum = 0

    private var mCount2Bigger = false

    init {
        calculateChangeNum(0)

        mTextColor = Color.parseColor(DEFAULT_TEXT_COLOR)

        mMinOffsetY = 0f
        mMaxOffsetY = mTextSize

        mEndTextColor =
            Color.argb(0, Color.red(mTextColor), Color.green(mTextColor), Color.blue(mTextColor))

        mTextPaint = Paint()
        mTextPaint.isAntiAlias = true
        mTextPaint.textSize = mTextSize
        mTextPaint.color = mTextColor
    }

    fun setCount(mCount: Int) {
        this.mCount = mCount
        calculateChangeNum(0)
        requestLayout()
    }

    fun setMaxCount(mMaxCount: Int) {
        this.mMaxCount = mMaxCount
    }


    fun setTextSize(mTextSize: Float) {
        this.mTextSize = mTextSize
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            TuvUtils.getDefaultSize(
                widthMeasureSpec,
                getContentWidth() + paddingLeft + paddingRight * 2
            ),
            TuvUtils.getDefaultSize(
                heightMeasureSpec,
                getContentHeight() + paddingTop + paddingBottom
            )
        )
    }

    private fun getContentWidth(): Int {
        return ceil(mTextPaint.measureText("$mCount   /  $mMaxCount"))
            .toInt()
    }

    private fun getContentHeight(): Int {
        return mTextSize.toInt()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        calculateLocation()
    }

    private fun calculateLocation() {
        val text = mCount.toString()
        val textWidth = mTextPaint.measureText(text) / text.length
        val unChangeWidth = textWidth * (mTexts[0]?.length ?: 0)
        val fontMetrics = mTextPaint.fontMetricsInt
        val y =
            (paddingTop + (getContentHeight() - fontMetrics.bottom - fontMetrics.top) / 2).toFloat()
        mTextPoints[0].x = paddingLeft.toFloat()
        mTextPoints[1].x = paddingLeft + unChangeWidth
        mTextPoints[2].x = paddingLeft + unChangeWidth
        mTextPoints[3].x = paddingLeft + unChangeWidth + (1 - mFraction) * (textWidth * mCarryNum)
        mTextPoints[0].y = y
        mTextPoints[1].y = y - mOldOffsetY
        mTextPoints[2].y = y - mNewOffsetY
        mTextPoints[3].y = y
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //不变的部分
        mTextPaint.color = mTextColor
        canvas.drawText(mTexts[0].toString(), mTextPoints[0].x, mTextPoints[0].y, mTextPaint)

        //变化前部分
        mTextPaint.color = (TuvUtils.evaluate(mFraction, mEndTextColor, mTextColor) as Int)
        canvas.drawText(mTexts[1].toString(), mTextPoints[1].x, mTextPoints[1].y, mTextPaint)

        //变化后部分
        mTextPaint.color = (TuvUtils.evaluate(mFraction, mTextColor, mEndTextColor) as Int)
        canvas.drawText(mTexts[2].toString(), mTextPoints[2].x, mTextPoints[2].y, mTextPaint)

        //后缀
        mTextPaint.color = mTextColor
        canvas.drawText("   / $mMaxCount", mTextPoints[3].x, mTextPoints[3].y, mTextPaint)
    }

    /**
     * 计算不变，原来，和改变后各部分的数字
     * 这里是只针对加一和减一去计算的算法，因为直接设置的时候没有动画
     */
    fun calculateChangeNum(change: Int) {
        if (change == 0) {
            mTexts[0] = mCount.toString()
            mTexts[1] = ""
            mTexts[2] = ""
            return
        }

        val oldNum = mCount.toString()
        val newNum = (mCount + change).toString()

        mCarryNum = newNum.length - oldNum.length

        for (i in oldNum.indices) {
            val oldC = oldNum[i]
            val newC = newNum[i]
            if (oldC != newC) {
                mTexts[0] = if (i == 0) "" else newNum.substring(0, i)
                mTexts[1] = oldNum.substring(i)
                mTexts[2] = newNum.substring(i)
                break
            }
        }
        mCount += change
        startAnim(change > 0)
    }

    private fun startAnim(is2Bigger: Boolean) {
        mCount2Bigger = is2Bigger
        val textOffsetY = ObjectAnimator.ofFloat(
            this, "textOffsetY",
            mMinOffsetY, if (mCount2Bigger) mMaxOffsetY else -mMaxOffsetY
        )
        textOffsetY.duration = COUNT_ANIM_DURING.toLong()
        textOffsetY.start()
    }

    fun setTextOffsetY(offsetY: Float) {
        mOldOffsetY = offsetY //变大是从[0,1]，变小是[0,-1]
        mNewOffsetY = if (mCount2Bigger)
        {
            //从下到上[-1,0]
            offsetY - mMaxOffsetY
        } else
        {
            //从上到下[1,0]
            mMaxOffsetY + offsetY
        }
        mFraction = (mMaxOffsetY - abs(mOldOffsetY)) / (mMaxOffsetY - mMinOffsetY)
        calculateLocation()
        postInvalidate()
    }

}