package com.example.customeview.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {
    /**
     * 存储所有的View，按行记录
     */
    private List<List<View>> mAllViews = new ArrayList<List<View>>();
    /**
     * 记录每一行的最大高度
     */
    private List<Integer> mLineHeight = new ArrayList<Integer>();

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获得它的父容器对它的期望测量模式和大小
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int Max_LineWidth = 0;
        int Max_LineHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View ChildView = getChildAt(i);
            //测量孩子
            measureChild(ChildView, widthMeasureSpec, heightMeasureSpec);
            //拿到孩子的布局参数,若父亲的Mode不是Exactly则要根据这个来测量父亲
            MarginLayoutParams lp = (MarginLayoutParams) ChildView
                    .getLayoutParams();
            // 获得当前孩子占据的宽度
            int childWidth = ChildView.getMeasuredWidth() + lp.leftMargin
                    + lp.rightMargin;
            // 获得当前孩子占据的高度
            int childHeight = ChildView.getMeasuredHeight() + lp.topMargin
                    + lp.bottomMargin;
            //接下来获得父亲为了布局孩子所可能需要的最大宽度与最大高度
            //若父亲的Mode是Exactly则用不到
            int CurrentLineWidth = 0;
            int CurrentLineMaxHeight = 0;
            if (childWidth + CurrentLineWidth > sizeWidth) {
                Max_LineWidth = Math.max(CurrentLineWidth, childWidth);
                CurrentLineWidth = childWidth;
                Max_LineHeight += CurrentLineMaxHeight;
                CurrentLineMaxHeight = childHeight;
            } else {
                CurrentLineWidth += childWidth;
                CurrentLineMaxHeight = Math.max(CurrentLineMaxHeight, childHeight);
            }
            //如果是最后一个孩子,且childWidth + CurrentLineWidth > sizeWidth不成立,则需要执行以下判断
            if(i==getChildCount()-1){
                Max_LineWidth=Math.max(Max_LineWidth,CurrentLineWidth);
                Max_LineHeight+=CurrentLineMaxHeight;
            }
        }
        //测量父亲
        setMeasuredDimension((modeWidth == MeasureSpec.EXACTLY) ? sizeWidth
                : Max_LineWidth, (modeHeight == MeasureSpec.EXACTLY) ? sizeHeight
                : Max_LineHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllViews.clear();
        mLineHeight.clear();

        int width = getWidth();

        int lineWidth = 0;
        int lineHeight = 0;

        // 存储每一行所有的childView
        List<View> lineViews = new ArrayList<View>();
        //遍历孩子拿到参数来摆放孩子
        for(int i=0;i<getChildCount();i++){
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child
                    .getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            // 如果已经需要换行
            if (childWidth + lp.leftMargin + lp.rightMargin + lineWidth > width) {
                // 记录这一行所有的View以及最大高度
                mLineHeight.add(lineHeight);
                // 将当前行的childView保存，然后开启新的ArrayList保存下一行的childView
                mAllViews.add(lineViews);
                lineWidth = 0;
                // 重置行宽
                lineViews = new ArrayList<View>();
            } else {
                lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
                lineHeight = Math.max(lineHeight, childHeight + lp.topMargin
                        + lp.bottomMargin);
                lineViews.add(child);
                if(i==getChildCount()-1){
                    // 记录最后一行
                    mLineHeight.add(lineHeight);
                    mAllViews.add(lineViews);
                }
            }
        }
        int left = 0;
        int top = 0;
        for (int i = 0; i < mAllViews.size(); i++) {
            // 每一行的所有的views
            lineViews = mAllViews.get(i);
            // 当前行的最大高度
            lineHeight = mLineHeight.get(i);

            // 遍历当前行所有的View
            for (int j = 0; j < lineViews.size(); j++) {
                View child = lineViews.get(j);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }
                MarginLayoutParams lp = (MarginLayoutParams) child
                        .getLayoutParams();

                //计算childView的left,top,right,bottom
                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();

                child.layout(lc, tc, rc, bc);

                left += child.getMeasuredWidth() + lp.rightMargin
                        + lp.leftMargin;
            }
            left = 0;
            top += lineHeight;
        }

    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
