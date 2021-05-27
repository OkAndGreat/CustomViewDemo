package com.example.customview.TextMeasure

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.customview.R
import com.example.customview.dp

private val IMAGE_SIZE = 150.dp
private val IMAGE_PADDING = 50.dp

class MultilineTextView(context: Context, attrs: AttributeSet?) :
        View(context, attrs) {
    private val text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur tristique urna tincidunt maximus viverra. Maecenas commodo pellentesque dolor ultrices porttitor. Vestibulum in arcu rhoncus, maximus ligula vel, consequat sem. Maecenas a quam libero. Praesent hendrerit ex lacus, ac feugiat nibh interdum et. Vestibulum in gravida neque. Morbi maximus scelerisque odio, vel pellentesque purus ultrices quis. Praesent eu turpis et metus venenatis maximus blandit sed magna. Sed imperdiet est semper urna laoreet congue. Praesent mattis magna sed est accumsan posuere. Morbi lobortis fermentum fringilla. Fusce sed ex tempus, venenatis odio ac, tempor metus."
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 16.dp
    }

    private val bitmap = getAvatar(IMAGE_SIZE.toInt())
    private val fontMetrics = Paint.FontMetrics()

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(bitmap, width - IMAGE_SIZE, IMAGE_PADDING, paint)
        paint.getFontMetrics(fontMetrics)
        val measureWidth= floatArrayOf(0f)
        var start=0
        var count:Int
        var verticalOffset=-fontMetrics.top
        var maxWidth:Float
        while(start<text.length){
            maxWidth=if(verticalOffset+fontMetrics.bottom< IMAGE_PADDING||verticalOffset+fontMetrics.top> IMAGE_PADDING+ IMAGE_SIZE){
                width.toFloat()
            }else{
                width.toFloat()- IMAGE_SIZE
            }
            //这里可以得到当maxwidth时text最多可以画多个个字符
            count=paint.breakText(text,start,text.length,true,maxWidth,measureWidth)
            canvas.drawText(text,start,start+count,0f,verticalOffset,paint)
            start+=count
            verticalOffset+=paint.fontSpacing
        }

    }

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