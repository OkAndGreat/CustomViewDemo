package com.example.customeview.TextChangeView.mine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.customeview.R
import com.example.customeview.show

class MyTextChangeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_text_change)
        val tv = findViewById<MyThumbUpView>(R.id.ThumbView)
        tv.setThumbUpClickListener(object :MyThumbView.ThumbUpClickListener{
            override fun thumbUpFinish() {
                //"thumbUpFinish".show(this@MyTextChangeActivity)
            }

            override fun thumbDownFinish() {
                //"thumbDownFinish".show(this@MyTextChangeActivity)
            }
        })
    }
}