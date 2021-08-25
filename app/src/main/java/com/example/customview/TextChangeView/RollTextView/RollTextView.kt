package com.example.customview.TextChangeView.RollTextView

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.example.customview.R
import kotlin.math.abs
import kotlin.math.ceil

/**
 * Author by OkAndGreat，Date on 2021/8/11.
 * 数字改变时滚动的TextView
 */
class RollTextView(context: Context, attrs: AttributeSet) :
    View(context, attrs) {
    companion object {
        //文字颜色值
        const val DEFAULT_TEXT_COLOR = "#FFFFFF"

        //文字大小
        const val DEFAULT_TEXT_SIZE = 40f

        //动画时间
        const val COUNT_ANIM_DURING = 2000
    }

    private var mTextPaint: Paint
    private var mTextSize = DEFAULT_TEXT_SIZE
    private var mTextColor = 0
    private var mEndTextColor = 0

    //当前值
    private var mCount = 0

    //最大值
    private var mMaxCount = 0

    //mTexts[0]是不变的部分，mTexts[1]原来的部分，mTexts[2]变化后的部分
    private var mTexts: Array<String?> = arrayOfNulls(3)

    //表示各部分的坐标
    private var mTextPoints
            : Array<ProgressPoint> = arrayOf(
        ProgressPoint(),
        ProgressPoint(),
        ProgressPoint(),
        ProgressPoint()
    )

    private var mMaxOffsetY = 0f
    private var mMinOffsetY = 0f

    private var mOldOffsetY = 0f
    private var mNewOffsetY = 0f
    private var mFraction = 0f

    //当当前数量增加时发生了进位时进位的数量
    //如：
    //如果 当前数量从 3变化到 7则 mCarryNum为0因为此时没有发生进位
    //如果 当前数量从 3变化到 17则 mCarryNum为1因为此时进位的数量为1
    //如果 当前数量从 3变化到 107则 mCarryNum为2因为此时进位的数量为2
    private var mCarryNum = 0

    //初始化画笔属性和要做动画要用到的的一些值
    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RollTextView)
        initCount(typedArray.getInt(R.styleable.RollTextView_Roll_count,0))
        mTextColor = typedArray.getColor(R.styleable.RollTextView_Roll_text_color,Color.parseColor(
            DEFAULT_TEXT_COLOR
        ))
        mTextSize = typedArray.getDimension(R.styleable.RollTextView_Roll_text_size,
            DisplayUtils.sp2px(context, 45F).toFloat())
        typedArray.recycle()

        mMinOffsetY = 0f
        mMaxOffsetY = mTextSize

        mEndTextColor =
            Color.argb(0, Color.red(mTextColor), Color.green(mTextColor), Color.blue(mTextColor))

        mTextPaint = Paint()
        mTextPaint.isAntiAlias = true
        mTextPaint.textSize = mTextSize
        mTextPaint.color = mTextColor
        mTextPaint.typeface = ResourcesCompat.getFont(context, R.font.bauhaus_93)
    }

    //设定一个初始值
    fun initCount(mCount: Int) {
        this.mCount = mCount
        mTexts[0] = mCount.toString()
        mTexts[1] = ""
        mTexts[2] = ""
        requestLayout()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            ProgressUtils.getDefaultSize(
                widthMeasureSpec,
                getContentWidth() + paddingLeft + paddingRight
            ),
            ProgressUtils.getDefaultSize(
                heightMeasureSpec,
                getContentHeight() + paddingTop + paddingBottom
            )
        )
    }

    private fun getContentWidth(): Int {
        return ceil(mTextPaint.measureText(mCount.toString()))
            .toInt()
    }

    private fun getContentHeight(): Int {
        return mTextSize.toInt()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        calculateLocation()
    }

    //计算各个Text所应该在的点得x,y值
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
        mTextPoints[0].y = y
        mTextPoints[1].y = y - mOldOffsetY
        mTextPoints[2].y = y - mNewOffsetY
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //不变的部分
        mTextPaint.color = mTextColor
        canvas.drawText(mTexts[0].toString(), mTextPoints[0].x, mTextPoints[0].y, mTextPaint)

        //变化前部分
        mTextPaint.color = (ProgressUtils.evaluate(mFraction, mEndTextColor, mTextColor) as Int)
        canvas.drawText(mTexts[1].toString(), mTextPoints[1].x, mTextPoints[1].y, mTextPaint)

        //变化后部分
        mTextPaint.color = (ProgressUtils.evaluate(mFraction, mTextColor, mEndTextColor) as Int)
        canvas.drawText(mTexts[2].toString(), mTextPoints[2].x, mTextPoints[2].y, mTextPaint)

    }

    /**
     * 此方法用来改变数字
     * 只能增加，不能减少
     */
    fun calculateChangeNum(change: Int) {
        val oldNum = mCount.toString()
        val newNum = (mCount + change).toString()

        mCarryNum = newNum.length - oldNum.length

        for (i in oldNum.indices) {
            val oldC = oldNum[i]
            val newC = newNum[i]
            if (oldC != newC || change == 0) {
                mTexts[0] = if (i == 0) "" else newNum.substring(0, i)
                mTexts[1] = oldNum.substring(i)
                mTexts[2] = newNum.substring(i)
                break
            }
        }
        mCount += change
        if (change >= 0) {
            startAnim(change != 0)
        }
    }

    private fun startAnim(needAnim: Boolean) {
        val textOffsetY = ObjectAnimator.ofFloat(
            this, "textOffsetY",
            mMinOffsetY, mMaxOffsetY
        )
        textOffsetY.duration = COUNT_ANIM_DURING.toLong()
        if (needAnim) textOffsetY.start()
    }

    fun setTextOffsetY(offsetY: Float) {
        mOldOffsetY = offsetY //变大是从[0,1]，变小是[0,-1]
        mNewOffsetY = offsetY - mMaxOffsetY
        mFraction = (mMaxOffsetY - abs(mOldOffsetY)) / (mMaxOffsetY - mMinOffsetY)
        calculateLocation()
        postInvalidate()
    }


}