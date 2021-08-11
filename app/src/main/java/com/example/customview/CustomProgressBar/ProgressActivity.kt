package com.example.customview.CustomProgressBar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.customview.R
import kotlinx.android.synthetic.main.activity_progress.*

class ProgressActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress)
        progressLayout1.setMaxCount(15)
        progressLayout1.post { progressLayout1.setCurCount(0)}

        progressLayout2.setMaxCount(15)
        progressLayout2.post { progressLayout2.setCurCount(2)}
    }

}