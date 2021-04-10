package com.example.customeview.CustomViewDrawing

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.customeview.px
import kotlin.math.cos
import kotlin.math.sin

/**
 * 体表盘View
 */
private const val OPEN_ANGLE = 120f
private const val MARK = 10
private val RADIUS = 150f.px
private val LENGTH = 120f.px

//刻度的长度与宽度
private val DASH_WIDTH = 2f.px
private val DASH_LENGTH = 10f.px

class DashboardView(context: Context?, attrs: AttributeSet?) :
        View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()
    private val dash = Path()
    private lateinit var pathEffect: PathEffect

    companion object {
        private const val TAG = "DashboardView"
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.d(TAG, "onMeasure: ")
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    init {
        Log.d(TAG, "init: ")
        paint.strokeWidth = 3f.px
        paint.style = Paint.Style.STROKE
        dash.addRect(0f, 0f, DASH_WIDTH, DASH_LENGTH, Path.Direction.CCW)

    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        Log.d(TAG, "onLayout: start")
        super.onLayout(changed, left, top, right, bottom)
        Log.d(TAG, "onLayout: end")
    }

    //在控件大小发生改变时会被调用，View的Layout（）方法中会调用这个方法
    //调用顺序 init()->onMeasure()→onSizeChanged()→onLayout()→onMeasure()→onLayout()→onDraw()
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        Log.d(TAG, "onSizeChanged: ")
        //！！注意这段代码不能放在init中 这是因为这时View还没有测量 getWidth是不正确的
        path.reset()
        //注意最后一个参数是sweepAngle而不是endAngle
        path.addArc(width / 2f - RADIUS, height / 2f - RADIUS, width / 2f + RADIUS, height / 2f + RADIUS, 90 + OPEN_ANGLE / 2f, 360 - OPEN_ANGLE)
        val pathMeasure = PathMeasure(path, false)
        //advance 间隔多少画一次 phase 提前量 隔了多少长度我们才开始画 注意刻度也有长度
        pathEffect = PathDashPathEffect(dash, (pathMeasure.length - DASH_WIDTH) / 20f, 0f, PathDashPathEffect.Style.ROTATE)
    }

    override fun onDraw(canvas: Canvas) {
        Log.d(TAG, "onDraw: ")
        //首先画出仪表盘的弧,因为等下要用到这个弧的长度来画刻度，所以不用drawArc,而是用drawPath
        canvas.drawPath(path, paint)

        //画刻度
        paint.pathEffect = pathEffect
        canvas.drawPath(path, paint)
        paint.pathEffect = null

        //画pointer
        canvas.drawLine(width / 2f, height / 2f,
                width / 2f + LENGTH * cos(markToRadians(MARK)).toFloat(),
                height / 2f + LENGTH * sin(markToRadians(MARK)).toFloat(), paint)
    }


    private fun markToRadians(mark: Int) =
            Math.toRadians((90 + OPEN_ANGLE / 2f + (360 - OPEN_ANGLE) / 20f * mark).toDouble())
}