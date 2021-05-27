package com.example.customview.XFermode

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.customview.R
import com.example.customview.px

/**
 * 为什么要 Xfermode？为了把多次绘制进⾏「合成」，例如蒙版效果：⽤ A 的形状和 B 的图案
 */

//怎么做？
//Canvs.saveLayer() 把绘制区域拉到单独的离屏缓冲⾥
//绘制 A 图形
//⽤ Paint.setXfermode() 设置 Xfermode
//绘制 B 图形
//⽤ Paint.setXfermode(null) 恢复 Xfermode
//⽤ Canvas.restoreToCount() 把离屏缓冲中的合成后的图形放回绘制区域
private val IMAGE_WIDTH = 200f.px
private val XFERMODE = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

class AvatarView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    //要使用的离屏缓冲大小
    private lateinit var bounds: RectF

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        bounds = RectF(width / 2.toFloat()-IMAGE_WIDTH/2, height / 2.toFloat()-IMAGE_WIDTH/2, width / 2.toFloat() + IMAGE_WIDTH/2,
                height / 2.toFloat() + IMAGE_WIDTH/2)
    }

    override fun onDraw(canvas: Canvas) {
        //为什么要⽤ saveLayer() 才能正确绘制 ？
        //为了把需要互相作⽤的图形放在单独的位置来绘制，不会受 View 本身的影响。
        //如果不使⽤ saveLayer()，绘制的⽬标区域将总是整个 View 的范围，两个图形
        //的交叉区域就错误了。

        //Canvs.saveLayer() 把绘制区域拉到单独的离屏缓冲⾥

        val count = canvas.saveLayer(bounds, null)
        //绘制 A 图形
        canvas.drawOval(width / 2.toFloat()-IMAGE_WIDTH/2, height / 2.toFloat()-IMAGE_WIDTH/2, width / 2.toFloat() + IMAGE_WIDTH/2,
                height / 2.toFloat() + IMAGE_WIDTH/2, paint)
        //⽤ Paint.setXfermode() 设置 Xfermode
        paint.xfermode = XFERMODE
        //绘制 B 图形
        canvas.drawBitmap(getAvatar(IMAGE_WIDTH.toInt()), width/2.toFloat()-IMAGE_WIDTH/2, height/2.toFloat()-IMAGE_WIDTH/2, paint)
        //⽤ Paint.setXfermode(null) 恢复 Xfermode
        paint.xfermode = null
        //⽤ Canvas.restoreToCount() 把离屏缓冲中的合成后的图形放回绘制区域
        canvas.restoreToCount(count)
    }

    //表示对超出需求大小的图片合适加载
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