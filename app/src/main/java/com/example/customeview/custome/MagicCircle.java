package com.example.customeview.custome;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class MagicCircle extends View {

    private Path mPath;
    private Paint mFillCirclePaint;

    /**
     * View的宽度
     **/
    private int width;
    /**
     * View的高度，这里View应该是正方形，所以宽高是一样的
     **/
    private int height;

    private float maxLength;
    private float mInterpolatedTime;
    private float stretchDistance;
    private float cDistance;
    private float radius;
    private float c;
    private float blackMagic = 0.551915024494f;   //这是三次贝塞尔式的常量，详解请见:http://spencermortensen.com/articles/bezier-circle/
    /*
     * 下面定义了p1, p2, p3, p4，四个对象，人要想明明是四个控制点，为什么要定义四个对象呢，因为这里是为了把这四个点和他们的辅助点联系在一起，
     * 所以总共应该是四个控制点和八个辅助点
     * */
    private VPoint p2, p4;   //定义了p2，p4两个对象，即圆的上面两个点，一个是右边的点，一个是左边的点
    private HPoint p1, p3;  //定义了p1，p3两个对象，即圆的上面两个点，一个是下边的点，一个是上面边的点


    public MagicCircle(Context context) {
        this(context, null, 0);
    }

    public MagicCircle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MagicCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        /*
         * 下面设置一些这个圆的参数，包括颜色，圆边框宽度(因为圆被填充了，所以也看不出来),还有抗锯齿
         * */
        mFillCirclePaint = new Paint();
        mFillCirclePaint.setColor(0xFFfe626d);
        mFillCirclePaint.setStyle(Paint.Style.FILL);
        mFillCirclePaint.setStrokeWidth(1);
        mFillCirclePaint.setAntiAlias(true);
        mPath = new Path();
        p2 = new VPoint();
        p4 = new VPoint();

        p1 = new HPoint();
        p3 = new HPoint();
    }


    /*
     * 下面用来初始化一些数据
     * */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
        radius = 50;
        c = radius * blackMagic;
        stretchDistance = radius;
        cDistance = c * 0.45f;
        maxLength = width - radius / 8;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();   //每次绘制前要重置路径
        canvas.translate(radius, radius);  //这里将坐标中心点移至圆心，这样的话是分两步的，这里用来处理位移，下面的几个方法用来处理变形，好理解一些

        if (mInterpolatedTime >= 0 && mInterpolatedTime <= 0.2) {
            model1(mInterpolatedTime);
        } else if (mInterpolatedTime > 0.2 && mInterpolatedTime <= 0.5) {
            model2(mInterpolatedTime);
        } else if (mInterpolatedTime > 0.5 && mInterpolatedTime <= 0.8) {
            model3(mInterpolatedTime);
        } else if (mInterpolatedTime > 0.8 && mInterpolatedTime <= 0.9) {
            model4(mInterpolatedTime);
        } else if (mInterpolatedTime > 0.9 && mInterpolatedTime <= 1) {
            model5(mInterpolatedTime);
        }

        float offset = maxLength * (mInterpolatedTime - 0.2f);
        offset = offset > 0 ? offset : 0;
        /*
         * 下面四行用来将圆形进行水平滑动，大家可以先注释掉，然后就能更加清楚的看见圆形的变化过程，不受位移的影响了
         * */
        p1.adjustAllX(offset);
        p2.adjustAllX(offset);
        p3.adjustAllX(offset);
        p4.adjustAllX(offset);


        /*
         *经过上面的一系列判断和调整，四个点和八个辅助点的位置都固定好了，然后将圆分成四个部分，然后连接路径，最后绘制
         * */
        mPath.moveTo(p1.x, p1.y);
        mPath.cubicTo(p1.right.x, p1.right.y, p2.bottom.x, p2.bottom.y, p2.x, p2.y);
        mPath.cubicTo(p2.top.x, p2.top.y, p3.right.x, p3.right.y, p3.x, p3.y);
        mPath.cubicTo(p3.left.x, p3.left.y, p4.top.x, p4.top.y, p4.x, p4.y);
        mPath.cubicTo(p4.bottom.x, p4.bottom.y, p1.left.x, p1.left.y, p1.x, p1.y);

        canvas.drawPath(mPath, mFillCirclePaint);

    }

    /*
     * 给四个点做初始化，各自固定好位置，等下用来变形
     * */
    private void model0() {
        p1.setY(radius);
        p3.setY(-radius);
        p3.x = p1.x = 0;
        p3.left.x = p1.left.x = -c;
        p3.right.x = p1.right.x = c;

        p2.setX(radius);
        p4.setX(-radius);
        p2.y = p4.y = 0;
        p2.top.y = p4.top.y = -c;
        p2.bottom.y = p4.bottom.y = c;
    }

    /*
     * 初始化过后我们要做的第一件事就是移动p2这个点，将p2往右移，移动距离从0到stretchDistance * time * 5，也就是整个圆的半径大小(此时达到圆的最大横向长度，3倍圆半径)，
     * 给人的感觉是右边被拉了，其他不变，长度为一个半径
     * */
    private void model1(float time) {//0~0.2
        model0();

        p2.setX(radius + stretchDistance * time * 5);
    }

    /*
     *这里做了两个处理，一个是p1,p3横坐标的移动，也就是往右移，然后就是p2,p4的四个辅助点的纵坐标，这样的话移动过程中就会有弯曲的感觉了，只要做稍稍调整即可，
     * 观察的效果给人感觉是圆形从左往右移，长度为一个半径，左右有向内凹的感觉
     * */
    private void model2(float time) {//0.2~0.5
        model1(0.2f);
        time = (time - 0.2f) * (10f / 3);
        p1.adjustAllX(stretchDistance / 2 * time);
        p3.adjustAllX(stretchDistance / 2 * time);
        p2.adjustY(cDistance * time);
        p4.adjustY(cDistance * time);
    }

    /*
     * 这里的处理和上面其实是差不多的，只不过全部反过来处理了，易懂，最后还移动p4的位置，保持圆的横向长度不变(此时圆横向长度应为3倍圆半径)，
     * 左右两边给人凸起来的感觉
     * */
    private void model3(float time) {//0.5~0.8
        model2(0.5f);
        time = (time - 0.5f) * (10f / 3);
        p1.adjustAllX(stretchDistance / 2 * time);
        p3.adjustAllX(stretchDistance / 2 * time);
        p2.adjustY(-cDistance * time);
        p4.adjustY(-cDistance * time);

        p4.adjustAllX(stretchDistance / 2 * time);

    }

    /*
     * 这里将p4多往右边移动了一点，目的是为了等下的回弹，这样的话给人的感觉是比较正常的
     * */
    private void model4(float time) {//0.8~0.9
        model3(0.8f);
        time = (time - 0.8f) * 10;
        p4.adjustAllX(stretchDistance / 2 * time);
    }

    /*
     * 这里直接用函数回弹p4的坐标
     * */
    private void model5(float time) {
        model4(0.9f);
        time = time - 0.9f;
        p4.adjustAllX((float) (Math.sin(Math.PI * time * 10f) * (2 / 10f * radius)));
    }

    /*
     * 这个类用来定义p2和p4,因为只有这两个点拥有top和Botton这样的辅助点
     * */
    class VPoint {
        public float x;
        public float y;
        public PointF top = new PointF();
        public PointF bottom = new PointF();

        /*
         * 用来设置控制点和辅助点的起始位置
         * */
        public void setX(float x) {
            this.x = x;
            top.x = x;
            bottom.x = x;
        }

        /*
         * 仅控制辅助点的位置
         * */
        public void adjustY(float offset) {
            top.y -= offset;
            bottom.y += offset;
        }

        /*
         * 控制控制点和辅助点的位置
         * */
        public void adjustAllX(float offset) {
            this.x += offset;
            top.x += offset;
            bottom.x += offset;
        }
    }
    /*
     * 这个类用来定义p1和p2,因为只有这两个点拥有left和right这样的辅助点
     * */
    class HPoint {
        public float x;
        public float y;
        public PointF left = new PointF();
        public PointF right = new PointF();

        public void setY(float y) {
            this.y = y;
            left.y = y;
            right.y = y;
        }

        public void adjustAllX(float offset) {
            this.x += offset;
            left.x += offset;
            right.x += offset;
        }
    }

    /*
     * 下面的动画用来控制时间，然后动态刷新界面，从而达到流畅的效果
     * */
    private class MoveAnimation extends Animation {

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            mInterpolatedTime = interpolatedTime;
            invalidate();
        }
    }

    /*
     * 对外开放的函数，用来开始动画
     * */
    public void startAnimation() {
        mPath.reset();
        mInterpolatedTime = 0;
        MoveAnimation move = new MoveAnimation();
        move.setDuration(3000);  //用来设置动画时长
        move.setInterpolator(new AccelerateDecelerateInterpolator());
        move.setRepeatCount(Animation.INFINITE);
        move.setRepeatMode(Animation.REVERSE);
        startAnimation(move);
    }
}
