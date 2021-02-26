package com.example.customeview.custome;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.customeview.util.SizeUtils;

/**
 * @author wzt
 */
public class SpiderView extends View {
    private Paint radarPaint, valuePaint;
    private float radius;
    private int centerX;
    private int centerY;
    private int count=6;
    private float angle = (float) (Math.PI*2/count);
    private double[] data={2,5,1,6,4,5};
    private float maxValue=6;
    private Paint textPaint;

    private String[] titles=new String[]{"喝水", "吸烟", "呼吸", "Drink", "Smoke", "Breath"};

    public SpiderView(Context context) {
        this(context, null);
    }

    public SpiderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpiderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        radius = Math.min(h, w) / 2 * 0.7f;
        //中心坐标
        centerX = w / 2;
        centerY = h / 2;
        postInvalidate();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void init() {
        radarPaint = new Paint();
        radarPaint.setStyle(Paint.Style.STROKE);
        radarPaint.setColor(Color.BLACK);
        valuePaint = new Paint();
        valuePaint.setColor(Color.BLUE);
        valuePaint.setStyle(Paint.Style.FILL);
        textPaint = new Paint();
        textPaint.setColor(Color.RED);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(SizeUtils.dip2px(getContext(),15));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制蜘蛛网格
        drawPolygon(canvas);
        //画网格中线
        drawLines(canvas);
        //画数据图
        drawRegion(canvas);
        //画文字
        drawText(canvas);

    }

    private void drawText(Canvas canvas) {
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;
        for (int i=0;i<count;i++){
            float x = (float) (centerX+(radius+fontHeight/2)*Math.cos(angle*i));
            float y = (float) (centerY+(radius+fontHeight/2)*Math.sin(angle*i));
            if(angle*i>=0&&angle*i<=Math.PI/2){
                //第4象限
                canvas.drawText(titles[i], x,y,textPaint);
            }else if(angle*i>=3*Math.PI/2&&angle*i<=Math.PI*2){
                //第3象限
                canvas.drawText(titles[i], x,y,textPaint);
            }else if(angle*i>Math.PI/2&&angle*i<=Math.PI){
                //第2象限
                float dis = textPaint.measureText(titles[i]);
                //文本长度
                canvas.drawText(titles[i], x-dis,y,textPaint);
            }else if(angle*i>=Math.PI&&angle*i<3*Math.PI/2){
                //第1象限
                float dis = textPaint.measureText(titles[i]);
                //文本长度
                canvas.drawText(titles[i], x-dis,y,textPaint);
            }
        }
    }

    private void drawRegion(Canvas canvas) {
        Path path = new Path();
        valuePaint.setAlpha(127);
        for(int i=0;i<count;i++){
            double percent = data[i]/maxValue;
            float x = (float) (centerX+radius*Math.cos(angle*i)*percent);
            float y = (float) (centerY+radius*Math.sin(angle*i)*percent);
            if(i==0){
                path.moveTo(x, centerY);
            }else{
                path.lineTo(x,y);
            }
            //绘制小圆点
            canvas.drawCircle(x,y,10,valuePaint);
        }
        //绘制填充区域
        valuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawPath(path, valuePaint);

    }

    private void drawLines(Canvas canvas) {
        Path path = new Path();
        float r = radius / (count);
        for (int i=0;i<count;i++){
            path.reset();
            float curR = r;
            float x = (float) (centerX + curR * Math.cos(angle * i));
            float y = (float) (centerY + curR * Math.sin(angle * i));
            float dx = (float) (centerX + curR * Math.cos(angle * i)*6);
            float dy = (float) (centerY + curR * Math.sin(angle * i)*6);
            path.moveTo(x,y);
            path.lineTo(dx,dy);
            canvas.drawPath(path,radarPaint);
        }
    }

    private void drawPolygon(Canvas canvas) {
        Path path = new Path();
        float r = radius / (count);
        for (int i = 1; i <= count; i++) {
            //中心点不用绘制
            float curR = r * i;
            //当前半径
            path.reset();
            for (int j = 0; j < count; j++) {
                if (j == 0) {
                    path.moveTo(centerX + curR, centerY);
                } else {
                    //根据半径，计算出蜘蛛丝上每个点的坐标
                    float x = (float) (centerX + curR * Math.cos(angle * j));
                    float y = (float) (centerY + curR * Math.sin(angle * j));
                    path.lineTo(x, y);
                }
            }
            path.close();//闭合路径
            canvas.drawPath(path, radarPaint);
        }
    }

    /**
     * ==========================================================
     * 让调用者自己设置相关属性
     */

    public void setTitles(String[] titles) {
        this.titles = titles;
    }

    public void setRadarPaint(Paint radarPaint) {
        this.radarPaint = radarPaint;
    }

    public void setValuePaint(Paint valuePaint) {
        this.valuePaint = valuePaint;
    }

    public void setData(double[] data) {
        this.data = data;
    }

    public void setTextPaint(Paint textPaint) {
        this.textPaint = textPaint;
    }
}
