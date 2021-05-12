package com.example.customeview.TextChangeView.mine

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.OvershootInterpolator
import com.example.customeview.R
import com.example.customeview.TextChangeView.Other.changed.TuvPoint
import com.example.customeview.TextChangeView.Other.changed.TuvUtils


class MyThumbView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    //圆圈颜色
    private val START_COLOR = Color.parseColor("#00e24d3d")
    private val END_COLOR = Color.parseColor("#88e24d3d")

    //缩放动画的时间
    private val SCALE_DURING = 3000

    //圆圈扩散动画的时间
    private val RADIUS_DURING = 2000

    private val SCALE_MIN = 0.9f
    private val SCALE_MAX = 1f

    private lateinit var mThumbUp: Bitmap
    private lateinit var mShining: Bitmap
    private lateinit var mThumbNormal: Bitmap
    private lateinit var mBitmapPaint: Paint

    private var mThumbWidth = 0f
    private var mThumbHeight = 0f
    private var mShiningWidth = 0f
    private var mShiningHeight = 0f

    private lateinit var mShiningPoint: TuvPoint
    private lateinit var mThumbPoint: TuvPoint
    private var mCirclePoint: TuvPoint

    private var mRadiusMax = 0f
    private var mRadiusMin = 0f
    private var mRadius = 0f
    private var mClipPath: Path
    private var mCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var mIsThumbUp = false
    private val mLastStartTime: Long = 0

    //点击的回调
    private lateinit var mThumbUpClickListener: ThumbUpClickListener

    private var mIsOnAnimation = false
    private var mThumbUpAnim: AnimatorSet? = null





    init {
        initBitmapInfo()
        mCirclePaint = Paint()
        mCirclePaint.isAntiAlias = true
        mCirclePaint.style = Paint.Style.STROKE
        mCirclePaint.strokeWidth = TuvUtils.dip2px(getContext(), 2f).toFloat()

        mCirclePoint = TuvPoint()
        mCirclePoint.x = mThumbPoint.x + mThumbWidth / 2
        mCirclePoint.y = mThumbPoint.y + mThumbHeight / 2

        mRadiusMax = Math.max(mCirclePoint.x - paddingLeft, mCirclePoint.y - paddingTop)
        mRadiusMin = TuvUtils.dip2px(getContext(), 8f).toFloat() //这个值是根据点击效果调整得到的

        mClipPath = Path()
        mClipPath.addCircle(mCirclePoint.x, mCirclePoint.y, mRadiusMax, Path.Direction.CW)
    }

    private fun initBitmapInfo() {
        mBitmapPaint = Paint()
        mBitmapPaint.isAntiAlias = true

        resetBitmap()

        mThumbWidth = mThumbUp.width.toFloat()
        mThumbHeight = mThumbUp.height.toFloat()

        mShiningWidth = mShining.width.toFloat()
        mShiningHeight = mShining.height.toFloat()

        mShiningPoint = TuvPoint()
        mThumbPoint = TuvPoint()
        //这个相对位置是在布局中试出来的
        //这个相对位置是在布局中试出来的
        mShiningPoint.x = (paddingLeft + TuvUtils.dip2px(context, 2f)).toFloat()
        mShiningPoint.y = paddingTop.toFloat()
        mThumbPoint.x = paddingLeft.toFloat()
        mThumbPoint.y = (paddingTop + TuvUtils.dip2px(context, 8f)).toFloat()
    }

    private fun resetBitmap() {
        mThumbUp = BitmapFactory.decodeResource(resources, R.drawable.ic_messages_like_selected)
        mThumbNormal =
            BitmapFactory.decodeResource(resources, R.drawable.ic_messages_like_unselected)
        mShining =
            BitmapFactory.decodeResource(resources, R.drawable.ic_messages_like_selected_shining)
    }

    //设置初始状态 mClickCount为1表示处于点赞状态否则为未点赞状态
    fun setIsThumbUp(isThumbUp: Boolean) {
        mIsThumbUp = isThumbUp
        postInvalidate()
    }

    fun setThumbUpClickListener(thumbUpClickListener: ThumbUpClickListener) {
        mThumbUpClickListener = thumbUpClickListener
    }

    fun getCirclePoint(): TuvPoint {
        return mCirclePoint
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            TuvUtils.getDefaultSize(
                widthMeasureSpec,
                getContentWidth() + getPaddingLeft() + getPaddingRight()
            ),
            TuvUtils.getDefaultSize(
                heightMeasureSpec,
                getContentHeight() + getPaddingTop() + getPaddingBottom()
            )
        )
    }

    private fun getContentWidth(): Int {
        val minLeft = Math.min(mShiningPoint.x, mThumbPoint.x)
        val maxRight = Math.max(mShiningPoint.x + mShiningWidth, mThumbPoint.x + mThumbWidth)
        return (maxRight - minLeft).toInt()
    }

    private fun getContentHeight(): Int {
        val minTop = Math.min(mShiningPoint.y, mThumbPoint.y)
        val maxBottom = Math.max(mShiningPoint.y + mShiningHeight, mThumbPoint.y + mThumbHeight)
        return (maxBottom - minTop).toInt()
    }

    //canvas.save();与canvas.restore();一般结合使用，
    //save()函数在前，.restore()函数在后，
    //用来保证在这两个函数之间所做的操作不会对原来在canvas上所画图形产生影响
    override fun onDraw(canvas: Canvas) {
        if (mIsThumbUp) {
            canvas.save()
            canvas.clipPath(mClipPath)
            canvas.drawBitmap(mShining, mShiningPoint.x, mShiningPoint.y, mBitmapPaint)
            canvas.restore()
            canvas.drawCircle(mCirclePoint.x, mCirclePoint.y, mRadius, mCirclePaint)
            canvas.drawBitmap(mThumbUp, mThumbPoint.x, mThumbPoint.y, mBitmapPaint)
        } else {
            canvas.drawBitmap(mThumbNormal, mThumbPoint.x, mThumbPoint.y, mBitmapPaint)
        }
    }

    //只有当mIsThumbUp才会执行动画，因为只有这时才表示上一次动画已经执行完
    fun startAnim() {
        if (!mIsOnAnimation) {
            mIsThumbUp = if (mIsThumbUp) {
                startThumbDownAnim()
                false
            } else {
                startThumbUpAnim()
                true
            }
        }

    }

    private fun startThumbDownAnim() {
        mIsOnAnimation = true
        val thumbUpScale =
            ObjectAnimator.ofFloat(this, "thumbUpScale", SCALE_MIN, SCALE_MAX)
        thumbUpScale.duration = SCALE_DURING.toLong()
        thumbUpScale.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                mIsThumbUp = false
                mIsOnAnimation = false
                setNotThumbUpScale(SCALE_MAX)
                mThumbUpClickListener.thumbDownFinish()
            }
        })
        thumbUpScale.start()
    }

    private fun startThumbUpAnim() {
        mIsOnAnimation = true
        if (mThumbUpAnim == null) {
            val notThumbUpScale = ObjectAnimator.ofFloat(
                this,
                "notThumbUpScale",
                SCALE_MAX,
                SCALE_MIN
            )
            notThumbUpScale.duration = SCALE_DURING.toLong()
            notThumbUpScale.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    mIsThumbUp = false
                    mIsThumbUp = true
                }
            })

            val thumbUpScale =
                ObjectAnimator.ofFloat(this, "thumbUpScale", SCALE_MIN, SCALE_MAX)
            thumbUpScale.duration = SCALE_DURING.toLong()
            thumbUpScale.interpolator = OvershootInterpolator()

            val circleScale = ObjectAnimator.ofFloat(this, "circleScale", mRadiusMin, mRadiusMax)
            circleScale.duration = RADIUS_DURING.toLong()

            mThumbUpAnim = AnimatorSet()
            mThumbUpAnim!!.play(thumbUpScale).with(circleScale)
            mThumbUpAnim!!.play(thumbUpScale).after(notThumbUpScale)
            mThumbUpAnim!!.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    mThumbUpClickListener.thumbUpFinish()
                }
            })
        }
        mThumbUpAnim!!.start()
    }

    private fun setNotThumbUpScale(scale: Float) {
        val matrix = Matrix()
        matrix.postScale(scale, scale)
        mThumbNormal =
            BitmapFactory.decodeResource(resources, R.drawable.ic_messages_like_unselected)
        mThumbNormal = Bitmap.createBitmap(
            mThumbNormal, 0, 0, mThumbNormal.width, mThumbNormal.height,
            matrix, true
        )
        postInvalidate()
    }

    private fun setThumbUpScale(scale: Float) {
        val matrix = Matrix()
        matrix.postScale(scale, scale)
        mThumbUp = BitmapFactory.decodeResource(resources, R.drawable.ic_messages_like_selected)
        mThumbUp = Bitmap.createBitmap(
            mThumbUp, 0, 0, mThumbUp.width, mThumbUp.height,
            matrix, true
        )
        postInvalidate()
    }

    private fun setShiningScale(scale: Float) {
        val matrix = Matrix()
        matrix.postScale(scale, scale)
        mShining =
            BitmapFactory.decodeResource(resources, R.drawable.ic_messages_like_selected_shining)
        mShining = Bitmap.createBitmap(
            mShining, 0, 0, mShining.width, mShining.height,
            matrix, true
        )
        postInvalidate()
    }

    fun setCircleScale(radius: Float) {
        mRadius = radius
        mClipPath = Path()
        mClipPath.addCircle(mCirclePoint.x, mCirclePoint.y, mRadius, Path.Direction.CW)
        val fraction = (mRadiusMax - radius) / (mRadiusMax - mRadiusMin)
        mCirclePaint.color =
            TuvUtils.evaluate(fraction, START_COLOR, END_COLOR) as Int
        postInvalidate()
    }

    interface ThumbUpClickListener {
        //点赞回调
        fun thumbUpFinish()

        //取消回调
        fun thumbDownFinish()
    }
}