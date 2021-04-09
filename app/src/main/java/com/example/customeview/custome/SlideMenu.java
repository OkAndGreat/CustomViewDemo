package com.example.customeview.custome;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Scroller;

import kotlin.reflect.KVariance;


/**
 * @author OkAndGreat
 */
public class SlideMenu extends ViewGroup {
    private static final String TAG = "SlideMenu";
    private final Context mContext;
    private int mSlideViewWidth;
    private View mSlideView;
    private View mContentView;
    Scroller mScroller;

    private int mLastX;
    private int mStart;
    private int mEnd;
    private int mWidth;
    private int scaledTouchSlop;
    private int dx;

    public SlideMenu(Context context) {
        this(context, null);
    }

    public SlideMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mScroller = new Scroller(getContext());
        scaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        getScreenWidth(context);
    }

    private void getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        mWidth = dm.heightPixels;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mContentView = getChildAt(0);
        measureChild(mContentView, widthMeasureSpec, heightMeasureSpec);
        mSlideView = getChildAt(1);
        mSlideViewWidth = mSlideView.getLayoutParams().width;
        int SlideViewWidthSpec = MeasureSpec.makeMeasureSpec(mSlideViewWidth, MeasureSpec.EXACTLY);
        measureChild(mSlideView, SlideViewWidthSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mContentView.layout(l, t, r, b);
        mSlideView.layout(r, t, r + mSlideView.getMeasuredWidth(), b);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        if (action == MotionEvent.ACTION_DOWN) {
            mLastX = x;
            mStart = getScrollX();
        } else if (action == MotionEvent.ACTION_MOVE) {
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
            }
            dx = mLastX - x;
            //防止向右滑动出现空白页面
            if (dx < -getScrollX()) {
                dx = 0;
            }
            if (getScrollX() >= 0 && getScrollX() <= mSlideViewWidth) {
                //移动的如果是正数,屏幕向右移动
                //RestWidth是为了防止向右滑动出现空白页面
                int RestWidth = mSlideViewWidth - getScrollX();
                Log.d(TAG, "onTouchEvent: dx-->"+ dx);
                if (dx > RestWidth) {
                    dx = RestWidth;
                }
                scrollBy(dx, 0);

            }
            mLastX = x;
        } else if (action == MotionEvent.ACTION_UP) {
            mEnd = getScrollX();
            int dScrollX = mEnd - mStart;
            if (getScrollX() < 0) {
                mScroller.startScroll(getScrollX(), 0, -mEnd, 0);
            } else {
                if (dScrollX > 0) {
                    if (dScrollX < mSlideViewWidth / 3) {
                        mScroller.startScroll(getScrollX(), 0, -mEnd, 0);
                    } else {
                        mScroller.startScroll(getScrollX(), 0, mSlideViewWidth - mEnd, 0);
                    }
                } else {
                    if (-dScrollX < mSlideViewWidth / 3) {
                        mScroller.startScroll(getScrollX(), 0, -dScrollX, 0);
                    } else {
                        mScroller.startScroll(getScrollX(), 0, -(mSlideViewWidth + dScrollX), 0);
                    }
                }
            }
        }
        postInvalidate();
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(ev.getAction()==MotionEvent.ACTION_MOVE){
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
            postInvalidate();
        }
    }
}
