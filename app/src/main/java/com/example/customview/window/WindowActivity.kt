package com.example.customview.window

import android.graphics.PixelFormat
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.customview.R

class WindowActivity : AppCompatActivity(), View.OnTouchListener {
    private val TAG = "TestActivity"

    private lateinit var mCreateWindowButton: Button

    var downX = 0
    var downY = 0
    var rawX = 0
    var rawY = 0

    private lateinit var mFloatingButton: Button
    private lateinit var mLayoutParams: WindowManager.LayoutParams
    private lateinit var mWindowManager: WindowManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_window)
        initView()
    }

    private fun initView() {
        mCreateWindowButton = findViewById<View>(R.id.button1) as Button
        mWindowManager = getSystemService(WINDOW_SERVICE) as WindowManager
    }

    fun onButtonClick(v: View) {
        if (v === mCreateWindowButton) {
            mFloatingButton = Button(this)
            mFloatingButton.text = "click me"
            mFloatingButton.background = getDrawable(R.drawable.shape_et)
            mLayoutParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                0,
                0,
                PixelFormat.TRANSPARENT
            )
            mLayoutParams.flags = (WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
            mLayoutParams.type = 1
            mLayoutParams.gravity = Gravity.LEFT or Gravity.TOP
            mLayoutParams.x = 100
            mLayoutParams.y = 300
            mFloatingButton.setOnTouchListener(this)
            mWindowManager.addView(mFloatingButton, mLayoutParams)
        }
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        Log.d(TAG, "onTouch: onTouch be called")
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.rawX.toInt()
                downY = event.rawY.toInt()
                rawX = mLayoutParams.x
                rawY = mLayoutParams.y
            }
            MotionEvent.ACTION_MOVE -> {
                Log.d(TAG, "onTouch: event.x.toInt()-->${event.x.toInt()}")
                val dx = event.rawX.toInt() - downX
                val dy = event.rawY.toInt() - downY

                mLayoutParams.x = rawX + dx
                mLayoutParams.y = rawY + dy
                mWindowManager.updateViewLayout(mFloatingButton, mLayoutParams)
            }
            MotionEvent.ACTION_UP -> {
            }
        }
        return false
    }

//    override fun onTouch(v: View?, event: MotionEvent): Boolean {
//        val rawX = event.rawX.toInt()
//        val rawY = event.rawY.toInt()
//        when (event.action) {
//            MotionEvent.ACTION_DOWN -> {
//                downX = event.rawX.toInt()
//                downY = event.rawY.toInt()
//            }
//            MotionEvent.ACTION_MOVE -> {
//                mLayoutParams.x = rawX
//                mLayoutParams.y = rawY
//                mWindowManager.updateViewLayout(mFloatingButton, mLayoutParams)
//            }
//            MotionEvent.ACTION_UP -> {
//            }
//        }
//        return false
//    }

    override fun onDestroy() {
        try {
            mWindowManager.removeView(mFloatingButton)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
        super.onDestroy()
    }
}