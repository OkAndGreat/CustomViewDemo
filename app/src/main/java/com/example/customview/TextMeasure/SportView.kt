package com.example.customview.TextMeasure

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.example.customview.R
import com.example.customview.dp


private val CIRCLE_COLOR = Color.parseColor("#90A4AE")
private val HIGHLIGHT_COLOR = Color.parseColor("#FF4081")
private val RING_WIDTH = 20.dp
private val RADIUS = 150.dp
private val mHandler = Handler(Looper.myLooper()!!)
private val SWEEPANGLE = 225f
private var SWEEPEDANGLE = 0f

class SportView(context: Context, attrs: AttributeSet?) :
        View(context, attrs) {

    companion object {
        private const val TAG = "SportView"
    }


    private val runnable = Runnable {
        Log.d(TAG, "runnable: ")
        if (SWEEPEDANGLE <= SWEEPANGLE) {
            invalidate()
        }
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 100.dp
        //设置字体
        typeface = ResourcesCompat.getFont(context, R.font.font)
        //设置文字居中对齐
        //文字居什么对齐其实就是Baseline和基准点的关系
        textAlign = Paint.Align.CENTER
    }
    private lateinit var canvas: Canvas
    private val bounds = Rect()
    private val fontMetrics = Paint.FontMetrics()


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.d(TAG, "onDraw: ")
        this.canvas = canvas
        //绘制环
        paint.style = Paint.Style.STROKE
        paint.color = CIRCLE_COLOR
        paint.strokeWidth = RING_WIDTH
        canvas.drawCircle(width / 2f, height / 2f, RADIUS, paint)

        //绘制进度条，这里我们用Handler来动态绘制
        paint.color = HIGHLIGHT_COLOR
        paint.strokeCap = Paint.Cap.ROUND
        mHandler.postDelayed(runnable, 2)
        if (SWEEPANGLE - SWEEPEDANGLE >= 3) {
            SWEEPEDANGLE += 3
        } else {
            SWEEPEDANGLE += SWEEPANGLE - SWEEPEDANGLE
        }

        canvas.drawArc(width / 2f - RADIUS, height / 2f - RADIUS, width / 2f + RADIUS, height / 2f + RADIUS, -90f, SWEEPEDANGLE, false, paint)

        paint.getFontMetrics(fontMetrics)
        //绘制文字
        paint.textSize = 40.dp
        paint.style = Paint.Style.FILL
        //y值其实设置的是文字的基准线，如果是height / 2f会导致文字偏上
        canvas.drawText("当前湿度：72", width / 2f, height / 2f - (fontMetrics.ascent + fontMetrics.descent) / 2f, paint)
        //绘制文字2
        paint.textSize = 150.dp
        //paint.textAlign=Paint.Align.LEFT
        paint.getTextBounds("abab", 0, "abab".length, bounds)
        //bounds.top就是文字的高
        canvas.drawText("abab", - bounds.left.toFloat(), - bounds.top.toFloat(), paint)
        //绘制文字3
        paint.textSize = 15.dp
        paint.getTextBounds("abab", 0, "abab".length, bounds)
        canvas.drawText("abab", - bounds.left.toFloat(), - bounds.top.toFloat(), paint)

    }
}