package com.example.customview.CustomProgressBar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.customview.R
import kotlinx.android.synthetic.main.activity_progress.*

class ProgressActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress)
        progressLayout.setMaxCount(20)
        progressLayout.post {
            progressLayout.setCurCount(12)
        }

    }

}