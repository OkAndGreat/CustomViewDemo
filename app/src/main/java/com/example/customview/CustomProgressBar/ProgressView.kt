package com.example.customview.CustomProgressBar

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

private const val TAG = "ProgressView"

class ProgressView(context: Context) :
    View(context) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var mPaintStrokeWidth = 46.toFloat()

    //进度条背景颜色
    private val bgColor = -0x1e1a18

    //动画执行时间
    private val duration = 5000L

    // 进度条颜色
    private var progressColor = -0x994ee

    private var averageWidth = 0F

    private var maxCount = 0
    private var curCount = 0

    private lateinit var Animator: ValueAnimator

    private var curBarWidth = 0F


    init {
        mPaintStrokeWidth = DisplayUtils.dp2px(context,8F).toFloat()
        progressColor = Color.parseColor("#7D8AFF")
        //初始化画笔
        paint.apply {
            style = Paint.Style.FILL
            strokeWidth = mPaintStrokeWidth
            strokeCap = Paint.Cap.ROUND
            textAlign = Paint.Align.CENTER
        }
    }

    //将View的高度设置成边框的宽度
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //项目需求 宽度写死 150dp
        val width = DisplayUtils.dp2px(context, 150F)
        averageWidth = (width / maxCount).toFloat()
        setMeasuredDimension(width, mPaintStrokeWidth.toInt())
    }

    override fun onDraw(canvas: Canvas) {
        //View的宽度和高度
        val width = width
        val height = height

        //画进度条背景
        paint.color = bgColor
//        canvas.drawLine(
//            paddingLeft.toFloat(),
//            (height / 2).toFloat(),
//            width.toFloat(),
//            (height / 2).toFloat(),
//            paint
//        )
        canvas.drawRoundRect(
            RectF(
                paddingLeft.toFloat(),
                0F,
                width.toFloat(),
                mPaintStrokeWidth
            ), 15F, 15F,
            paint
        )

        //画进度条
        paint.color = progressColor
        if (curBarWidth != 0F)

            canvas.drawRoundRect(
                RectF(
                    paddingLeft.toFloat(),
                    0F,
                    curBarWidth,
                    mPaintStrokeWidth
                ), 15F, 15F,
                paint
            )

    }

    fun setCurCount(Count: Int) {
        startAnim(curCount, Count)
        curCount = Count
    }

    fun setMaxCount(Count: Int) {
        maxCount = Count
    }

    private fun startAnim(oldCount: Int, newCount: Int) {
        Animator = ValueAnimator.ofFloat(averageWidth * oldCount, averageWidth * newCount)
        Animator.duration = duration
        Animator.interpolator = AccelerateDecelerateInterpolator()
        Animator.addUpdateListener {
            curBarWidth = it.animatedValue as Float
            invalidate()
        }

        Animator.start()
    }

}