package com.example.customeview.custome;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.customeview.util.SizeUtils;

public class LoadingView extends View {
    private static final String TAG = "LoadingView";
    private final Paint paint = new Paint();
    private int widthPixels;
    private int heightPixels;
    private final Context context;
    private int CurrentBall = 0;
    private float TranslateHeight = 0;
    private Boolean First = true;
    private ValueAnimator valueAnimator;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        //处理 wrap_content问题
        int defaultDimension = SizeUtils.dip2px(context, 150);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(defaultDimension, defaultDimension);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(defaultDimension, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, defaultDimension);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        widthPixels = w;
        heightPixels = h;
    }


    private void initPaint() {
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (CurrentBall == 0) {
            canvas.drawCircle((float) widthPixels / 4, (float) heightPixels / 2 - TranslateHeight, 60F, paint);
            canvas.drawCircle((float) widthPixels * 3 / 4, (float) heightPixels / 2, 60F, paint);
        } else {
            canvas.drawCircle((float) widthPixels / 4, (float) heightPixels / 2, 60F, paint);
            canvas.drawCircle((float) widthPixels * 3 / 4, (float) heightPixels / 2 - TranslateHeight, 60F, paint);
        }
        if (First) {
            startAni();
        }

        super.onDraw(canvas);
    }

    private void startAni() {
        First = false;
        valueAnimator = ValueAnimator.ofInt(1, 100);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.setDuration(400).start();
        IntEvaluator intEvaluator = new IntEvaluator();
        valueAnimator.addUpdateListener(animation -> {
            int animatedValue = (Integer) animation.getAnimatedValue();
            float animatedFraction = animation.getAnimatedFraction();
            TranslateHeight = 150.0f * animatedFraction;
            postInvalidate();
        });
    }


    @Override
    protected void onDetachedFromWindow() {
        valueAnimator.cancel();
        super.onDetachedFromWindow();
    }
}
