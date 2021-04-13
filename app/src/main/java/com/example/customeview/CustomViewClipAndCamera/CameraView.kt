package com.example.customeview.CustomViewClipAndCamera

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.customeview.R
import com.example.customeview.dp

private val BITMAP_SIZE = 200.dp
private val BITMAP_PADDING = 100.dp

/**
 * 用逆向思考
 */
class CameraView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmap = getAvatar(BITMAP_SIZE.toInt())
    private val camera = Camera()

    init {
        camera.rotateX(30f)
        //相当于canvas沿着X轴向屏幕外移动30degree
        camera.setLocation(0f, 0f, -6 * resources.displayMetrics.density)
    }
    override fun onDraw(canvas: Canvas) {
        //上半部分
//        canvas.save()
//
//        canvas.restore()
        //下半部分,用逆向思考,从drawBitmap开始，然后从下往上敲代码
        canvas.save()
        canvas.translate(BITMAP_PADDING + BITMAP_SIZE / 2, BITMAP_PADDING + BITMAP_SIZE / 2)
        canvas.rotate(-30f)
        camera.applyToCanvas(canvas)
        //clip这里不是很懂
        canvas.clipRect(- BITMAP_SIZE, 0f, BITMAP_SIZE, BITMAP_SIZE)
        canvas.rotate(30f)
        canvas.translate(-(BITMAP_PADDING+ BITMAP_SIZE/2),-(BITMAP_PADDING+ BITMAP_SIZE/2))
        canvas.drawBitmap(bitmap, BITMAP_PADDING, BITMAP_PADDING,paint)
        canvas.restore()
    }

//    override fun onDraw(canvas: Canvas) {
//        // 上半部分
//        canvas.save()
//        canvas.translate(BITMAP_PADDING + BITMAP_SIZE / 2, BITMAP_PADDING + BITMAP_SIZE / 2)
//        canvas.rotate(-30f)
//        canvas.clipRect(- BITMAP_SIZE, - BITMAP_SIZE, BITMAP_SIZE, 0f)
//        canvas.rotate(30f)
//        canvas.translate(- (BITMAP_PADDING + BITMAP_SIZE / 2), - (BITMAP_PADDING + BITMAP_SIZE / 2))
//        canvas.drawBitmap(bitmap, BITMAP_PADDING, BITMAP_PADDING, paint)
//        canvas.restore()
//
//        // 下半部分
//        canvas.save()
//        canvas.translate(BITMAP_PADDING + BITMAP_SIZE / 2, BITMAP_PADDING + BITMAP_SIZE / 2)
//        canvas.rotate(-30f)
//        camera.applyToCanvas(canvas)
//        canvas.clipRect(- BITMAP_SIZE, 0f, BITMAP_SIZE, BITMAP_SIZE)
//        canvas.rotate(30f)
//        canvas.translate(- (BITMAP_PADDING + BITMAP_SIZE / 2), - (BITMAP_PADDING + BITMAP_SIZE / 2))
//        canvas.drawBitmap(bitmap, BITMAP_PADDING, BITMAP_PADDING, paint)
//        canvas.restore()
//    }

    private fun getAvatar(width: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, R.drawable.avatar_rengwuxian, options)
        options.inJustDecodeBounds = false
        options.inDensity = options.outWidth
        options.inTargetDensity = width
        return BitmapFactory.decodeResource(resources, R.drawable.avatar_rengwuxian, options)
    }
}