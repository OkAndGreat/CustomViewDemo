package com.example.customeview.SlideMenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * @author OkAndGreat
 */
public class SlideMenu extends ViewGroup {

    private int mSlideViewWidth;
    private View mSlideView;
    private View mContentView;
    private Context mContext;
    Scroller mScroller;
    private int mScrollXStart;
    private int mScrollXMove;
    private int mScrollXEnd;
    private int mLastX;

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
        mContentView.layout(0, 0, mContentView.getMeasuredWidth(), mContentView.getMeasuredHeight());
        mSlideView.layout(mContentView.getMeasuredWidth(), 0, mSlideView.getMeasuredWidth(), mSlideView.getMeasuredHeight());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            mLastX = x;
            mScrollXStart = getScrollX();
        } else if (action == MotionEvent.ACTION_MOVE) {
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
            }
            int dx = mLastX - x;
            if (getScrollX()>=0){
                scrollBy(dx,0);
            }
            mLastX = x;
        } else if (action == MotionEvent.ACTION_UP) {
            mScrollXEnd = getScrollX();
            int dX = mScrollXEnd - mScrollXStart;
            if (dX > mSlideViewWidth / 3) {
                mScroller.startScroll(mScrollXEnd, 0, mSlideViewWidth - (mScrollXEnd - mScrollXStart), 0);
            } else {
                mScroller.startScroll(mScrollXEnd, 0, -mScrollXEnd, 0);
            }
            if (-dX > mSlideViewWidth / 3) {
                mScroller.startScroll(mScrollXEnd, 0, mSlideViewWidth - (mScrollXStart - mScrollXEnd), 0);
            } else {
                mScroller.startScroll(mScrollXEnd, 0, -mScrollXEnd, 0);
            }
        }
        return true;
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
