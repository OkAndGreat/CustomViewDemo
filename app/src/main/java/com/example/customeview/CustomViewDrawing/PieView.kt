package com.example.customeview.CustomViewDrawing

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.customeview.px
import kotlin.math.cos
import kotlin.math.sin

/**
 * 饼状图View 并尝试将其中一块脱出
 */
private val RADIUS = 150f.px
private val ANGLES = floatArrayOf(60f, 90f, 150f, 60f)
private val COLORS = listOf(Color.parseColor("#C2185B"), Color.parseColor("#00ACC1"), Color.parseColor("#558B2F"), Color.parseColor("#5D4037"))
private val OFFSET_LENGTH = 20f.px

class PieView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas: Canvas) {
        //画弧
        var startAngle = 0f
        //这个方法相当于 for(int i=0;i<ANGLE.length;i++) 然后使用i及其对应的值
        for ((index, angle) in ANGLES.withIndex()) {
            paint.color = COLORS[index]
            if (index == 1||index==2) {
                canvas.save()
                //仔细体会这个canvas.translate方法为什么导致了将其中一块脱出的效果 其实可以看成将一块画好了我们将那一块进行移动 实际我们是通过移动画布来实现的
                canvas.translate(OFFSET_LENGTH * cos(Math.toRadians(startAngle + angle / 2f.toDouble())).toFloat(),
                        OFFSET_LENGTH * sin(Math.toRadians(startAngle + angle / 2f.toDouble())).toFloat())
            }
            canvas.drawArc(width / 2f - RADIUS, height / 2f - RADIUS, width / 2f + RADIUS, height / 2f + RADIUS, startAngle, angle, true, paint)
            startAngle += angle //这里注意drawArc里面的开始角度和结束角度表示什么意义
            if (index == 1||index==2) {
                canvas.restore()
            }
        }
    }
}