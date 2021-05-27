package com.example.customview.XFermode

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.customview.px

//怎么做？
//Canvs.saveLayer() 把绘制区域拉到单独的离屏缓冲⾥
//绘制 A 图形
//⽤ Paint.setXfermode() 设置 Xfermode
//绘制 B 图形
//⽤ Paint.setXfermode(null) 恢复 Xfermode
//⽤ Canvas.restoreToCount() 把离屏缓冲中的合成后的图形放回绘制区域


class XfermodeView(context: Context?, attrs: AttributeSet?):View(context,attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bounds = RectF(150f.px, 50f.px, 300f.px, 200f.px)
    private val circleBitmap = Bitmap.createBitmap(150f.px.toInt(), 150f.px.toInt(), Bitmap.Config.ARGB_8888)
    private val squareBitmap = Bitmap.createBitmap(150f.px.toInt(), 150f.px.toInt(), Bitmap.Config.ARGB_8888)
    private val XFERMODE = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    init {
        val canvas = Canvas(circleBitmap)
        paint.color = Color.parseColor("#D81B60")
        canvas.drawOval(50f.px, 0f.px, 150f.px, 100f.px, paint)
        paint.color = Color.parseColor("#2196F3")
        canvas.setBitmap(squareBitmap)
        canvas.drawRect(0f.px, 50f.px, 100f.px, 150f.px, paint)
    }
    override fun onDraw(canvas: Canvas) {
        val count = canvas.saveLayer(bounds, null)
        canvas.drawBitmap(circleBitmap, 150f.px, 50f.px, paint)
        paint.xfermode = XFERMODE
        canvas.drawBitmap(squareBitmap, 150f.px, 50f.px, paint)
        paint.xfermode = null
        canvas.restoreToCount(count)
    }
}