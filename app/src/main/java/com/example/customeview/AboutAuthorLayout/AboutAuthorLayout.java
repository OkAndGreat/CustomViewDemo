package com.example.customeview.AboutAuthorLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Scroller;

import androidx.annotation.Nullable;

public class AboutAuthorLayout extends ViewGroup {
    private int mHeight;
    Scroller mScroller;
    private int mLastY;
    private int mStart;
    private int mEnd;
    private Boolean mInPageOne=true;
    private Boolean mInPageTwo=false;

    public AboutAuthorLayout(Context context) {
        this(context, null);
    }

    public AboutAuthorLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AboutAuthorLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getScreenHeight(context);
        mScroller = new Scroller(getContext());
    }

    private void getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        mHeight = dm.heightPixels;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        MarginLayoutParams mlp = (MarginLayoutParams) getLayoutParams();
        mlp.height = mHeight * childCount;
        setLayoutParams(mlp);
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                child.layout(l, i * mHeight, r, (i + 1) * mHeight);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            mLastY = y;
            mStart = getScrollY();
        } else if (action == MotionEvent.ACTION_MOVE) {
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
            }
            int dy = mLastY - y;
            //当向上滑时只允许滑到屏幕宽度的十分之一,达到后停止滑动
            if (-getScrollY() <mHeight / 10||getScrollY()>0) {
                scrollBy(0, dy);
            }
            mLastY = y;
        } else if (action == MotionEvent.ACTION_UP) {
            mEnd = getScrollY();
            int dScrollY = mEnd - mStart;
            if (dScrollY > 0) {
                if (dScrollY < mHeight / 3) {
                    mScroller.startScroll(0, getScrollY(), 0, -dScrollY);
                } else {
                    //在第一页时向下滑才允许
                    if (mInPageOne == true) {
                        mInPageOne = false;
                        mInPageTwo=true;
                        mScroller.startScroll(0, getScrollY(), 0, mHeight - dScrollY);
                    }
                    else {
                        mScroller.startScroll(0, getScrollY(), 0, -dScrollY);
                    }
                }
            } else {
                if (-dScrollY < mHeight / 3) {
                    mScroller.startScroll(0, getScrollY(), 0, -dScrollY);
                } else {
                    //在第二页时向上滑才允许
                    if(mInPageTwo==true){
                        mInPageTwo=false;
                        mInPageOne =true;
                        mScroller.startScroll(0, getScrollY(), 0, -mHeight - dScrollY);
                    }else {
                        mScroller.startScroll(0, getScrollY(), 0, -dScrollY);
                    }
                }
            }
        }
        postInvalidate();
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            postInvalidate();
        }
    }

}
