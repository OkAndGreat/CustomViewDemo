package com.example.customeview.healthselector


import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Looper
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.OverScroller
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.ViewCompat
import com.example.customeview.R
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


private const val TAG = "WeightSelectorView"
private val mHandler = Handler(Looper.myLooper()!!)

class WeightSelectorView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var weight = 112.5
    private val gestureDetector = GestureDetectorCompat(context, GestureListener())

    //    private val scroller = OverScroller(context)
//    private val flingRunner = FlingRunner()
    private val fixRunner = FixRunner()
    private var offsetX = 0f


    init {
        paint.apply {
            textSize = 140.toFloat()
            textAlign = Paint.Align.CENTER
            strokeWidth = 12.toFloat()
            typeface = ResourcesCompat.getFont(context, R.font.font)
        }
    }

    override fun onDraw(canvas: Canvas) {
        //Log.d(TAG, "onDraw: ")
        val width = width
        val height = height
        //绘制背景和刻度
        //绘制背景
        paint.color = Color.rgb(246, 249, 246)
        canvas.drawRect(width / 2.toFloat() - 35000, height / 2.toFloat(), width / 2.toFloat() + 35000, height / 2 + 300.toFloat(), paint)

        //绘制刻度
        //25-200Kg 每1kg10个刻度 共10x175+1=1751个刻度线 1750个刻度
        paint.color = Color.rgb(222, 226, 222)
        for (i in 0 until 1751) {
            if (i % 10 == 0) canvas.drawLine(offsetX + width / 2.toFloat() - 35000 + i * 40, height / 2.toFloat(),
                    offsetX + width / 2.toFloat() - 35000 + i * 40, height / 2.toFloat() + 150.toFloat(), paint)
            else canvas.drawLine(offsetX + width / 2.toFloat() - 35000 + i * 40, height / 2.toFloat(),
                    offsetX + width / 2.toFloat() - 35000 + i * 40, height / 2.toFloat() + 100.toFloat(), paint)
        }


        //绘制文字和瞄准线
        paint.color = Color.rgb(73, 191, 118)
        canvas.drawText(String.format("%.1f", weight) + "Kg", width / 2.toFloat(), height / 2.toFloat() - 150, paint)
        canvas.drawLine(width / 2.toFloat(), height / 2.toFloat(), width / 2.toFloat(),
                height / 2.toFloat() + 160.toFloat(), paint)

    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)

        //滑动修复 防止瞄准线没滑动到刻度线上
        //由于Fling的原因不能马上修复
        if (event.actionMasked == MotionEvent.ACTION_UP) {
            mHandler.post(fixRunner)
        }
        return true
    }

    /**
     * 防止越界
     */
    private fun fixOffsets() {
        Log.d(TAG, "fixOffsets: offsetX-->$offsetX")
        offsetX = max(offsetX, (-35000).toFloat())
        offsetX = min(offsetX, 35000.toFloat())
        Log.d(TAG, "fixOffsets: offsetX%40 --> ${offsetX % 40}")
        //应该分正负情况
        weight = if (abs(offsetX % 40) <= 20) {
            112.5 - (offsetX.toInt() / 40 * 0.1)
        } else {
            if (offsetX < 0) {
                112.5 + (abs(offsetX).toInt() / 40 * 0.1) + 0.1
            } else {
                112.5 - (offsetX.toInt() / 40 * 0.1 + 0.1)
            }
        }
    }


    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            offsetX -= distanceX
            fixOffsets()
            invalidate()
            return false
        }


//        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
//            scroller.fling(offsetX.toInt(), 0, velocityX.toInt(), velocityY.toInt(),
//                    -35000, 35000, 0, 0)
//            ViewCompat.postOnAnimation(this@WeightSelectorView, flingRunner)
//            return false
//        }
    }
//
//    inner class FlingRunner : Runnable {
//        override fun run() {
//            if (scroller.computeScrollOffset()) {
//                offsetX = scroller.currX.toFloat()
//                fixOffsets()
//                invalidate()
//                ViewCompat.postOnAnimation(this@WeightSelectorView, this)
//            }
//        }
//    }

    inner class FixRunner : Runnable {
        override fun run() {
            if (offsetX % 40 != 0f) {
                if (abs(offsetX % 40) <= 20) {
                    if (abs(offsetX % 40) < 1f) {
                        if (offsetX % 40 < 0) {
                            offsetX += (offsetX % 40)
                        } else {
                            offsetX -= (offsetX % 40)
                        }
                        offsetX -= (offsetX % 40)
                    } else {
                        if (offsetX % 40 < 0) {
                            offsetX += 1
                        } else {
                            offsetX -= 1
                        }
                    }
                } else {
                    if (abs(offsetX % 40) < 1f) {
                        if (offsetX % 40 < 0) {
                            offsetX -= (offsetX % 40)
                        } else {
                            offsetX += (offsetX % 40)
                        }
                    } else {
                        if (offsetX % 40 < 0) {
                            offsetX -= 1
                        } else {
                            offsetX += 1
                        }
                    }
                }
                mHandler.postDelayed(fixRunner, 25)
                //Log.d(TAG, "run:  FixoffsetX-->$offsetX")
                invalidate()
            }
        }
    }

}