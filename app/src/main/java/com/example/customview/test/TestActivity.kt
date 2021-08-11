package com.example.customview.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.customview.R
import kotlinx.android.synthetic.main.activity_test.*

private const val TAG = "TestActivity"

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        haha.calculateChangeNum(11000)

    }
}