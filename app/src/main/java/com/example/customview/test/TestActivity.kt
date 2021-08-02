package com.example.customview.test

import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.customview.R
import com.example.customview.TextChangeView.Other.changed.TuvUtils
import kotlinx.android.synthetic.main.activity_test.*

private const val TAG = "TestActivity"

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)


        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter= adapter()
//        val animator = ValueAnimator.ofInt(
//            TuvUtils.dip2px(this, 100F),
//            TuvUtils.dip2px(this, 50F)
//        )
//        animator.duration = 2500
//        animator.addUpdateListener {
//            val animatedFraction = it.animatedFraction
//            val curValue = it.animatedValue as Int
//            Log.d(TAG, "onCreate:curValue-->${curValue} animatedFraction-->${animatedFraction}  ")
//            val layoutParams = constraintLayout.layoutParams
//            layoutParams.height = curValue
//            constraintLayout.layoutParams = layoutParams
//            textView3.alpha = (1 - animatedFraction)
//            textView4.alpha = (1 - animatedFraction)
//        }
//        animator.start()

    }
}