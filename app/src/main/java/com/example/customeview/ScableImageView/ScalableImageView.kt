package com.example.customeview.ScableImageView

import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.OverScroller
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.ViewCompat
import com.example.customeview.R
import com.example.customeview.dp
import kotlin.math.max
import kotlin.math.min

private val IMAGE_SIZE = 300.dp.toInt()
private const val EXTRA_SCALE_FACTOR = 1.5f

open class ScalableImageView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmap = getAvatar(resources, IMAGE_SIZE)
    private var smallScale = 0f
    private var bigScale = 0f
    private var offsetX = 0f
    private var offsetY = 0f
    private var originalOffsetX = 0f
    private var originalOffsetY = 0f
    private val gestureDetector = GestureDetectorCompat(context, GestureListener())
    private val scaleGestureDetector = ScaleGestureDetector(context, ScaleGestureListener())
    private var currentScale = 0f
        set(value) {
            field = value
            invalidate()
        }
    private var big = false
    private val FlingRunner = HenFlingRunner()
    private val scroller = OverScroller(context)
    private val scaleAnimator = ObjectAnimator.ofFloat(this, "currentScale", currentScale, bigScale)


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        originalOffsetX = (width - IMAGE_SIZE) / 2f
        originalOffsetY = (height - IMAGE_SIZE) / 2f

        if (bitmap.width / bitmap.height.toFloat() > width / height.toFloat()) {
            smallScale = width / bitmap.width.toFloat()
            bigScale = height / bitmap.height.toFloat() * EXTRA_SCALE_FACTOR
        } else {
            smallScale = height / bitmap.height.toFloat()
            bigScale = width / bitmap.width.toFloat() * EXTRA_SCALE_FACTOR
        }

        currentScale = smallScale
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        if(!scaleGestureDetector.isInProgress){
            gestureDetector.onTouchEvent(event)
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val scaleFraction = (currentScale - smallScale) / (bigScale - smallScale)
        canvas.translate(offsetX * scaleFraction, offsetY * scaleFraction)
        canvas.scale(currentScale, currentScale, width / 2f, height / 2f)
        canvas.drawBitmap(bitmap, originalOffsetX, originalOffsetY, paint)
    }

    /**
     * 防止越界
     */
    private fun fixOffsets() {
        offsetX = min(offsetX, (bitmap.width * bigScale - width) / 2)
        offsetX = max(offsetX, -(bitmap.width * bigScale - width) / 2)
        offsetY = min(offsetY, (bitmap.height * bigScale - height) / 2)
        offsetY = max(offsetY, -(bitmap.height * bigScale - height) / 2)
    }

    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            offsetX -= distanceX
            offsetY -= distanceY
            fixOffsets()
            invalidate()
            return false
        }

        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            if (big) {
                scroller.fling(offsetX.toInt(), offsetY.toInt(), velocityX.toInt(), velocityY.toInt(),
                        (- (bitmap.width * bigScale - width) / 2).toInt(),
                        ((bitmap.width * bigScale - width) / 2).toInt(),
                        (- (bitmap.height * bigScale - height) / 2).toInt(),
                        ((bitmap.height * bigScale - height) / 2).toInt()
                )
                ViewCompat.postOnAnimation(this@ScalableImageView, FlingRunner)
            }
            return false
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            big=!big
            if(big){
                offsetX = -(e.x - width / 2f)
                offsetY = -(e.y - height / 2f)
                fixOffsets()
                scaleAnimator.setFloatValues(currentScale,bigScale)
                scaleAnimator.start()
            }else{
                currentScale=smallScale
                scaleAnimator.setFloatValues(bigScale,currentScale)
                scaleAnimator.start()
            }
            return true
        }
    }

    inner class ScaleGestureListener:ScaleGestureDetector.OnScaleGestureListener{
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val tempCurrentScale = currentScale * detector.scaleFactor
            return if (tempCurrentScale < smallScale || tempCurrentScale > bigScale) {
                false
            } else {
                currentScale *= detector.scaleFactor // 0 1; 0 无穷
                scaleAnimator.setFloatValues(currentScale, bigScale)
                true
            }
        }

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            offsetX = -(detector.focusX - width / 2f)
            offsetY = -(detector.focusY - height / 2f)
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector?) {
        }

    }

    inner class HenFlingRunner : Runnable {
        override fun run() {
            if (scroller.computeScrollOffset()) {
                offsetX = scroller.currX.toFloat()
                offsetY = scroller.currY.toFloat()
                invalidate()
                ViewCompat.postOnAnimation(this@ScalableImageView, this)
            }
        }
    }

    private fun getAvatar(resources: Resources, width: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, R.drawable.avatar_rengwuxian, options)
        options.inJustDecodeBounds = false
        options.inDensity = options.outWidth
        options.inTargetDensity = width
        return BitmapFactory.decodeResource(resources, R.drawable.avatar_rengwuxian, options)
    }
}