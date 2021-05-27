package com.example.customview.viewimpl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import com.example.customview.R
import kotlinx.android.synthetic.main.activity_view_impl.*
import java.lang.Thread.sleep
import kotlin.concurrent.thread

class ViewImplActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_impl)

        thread {
            sleep(2000)
            textView.setOnClickListener {
                textView.text = "${SystemClock.uptimeMillis()}"
            }
        }

//        thread {
//            Looper.prepare()
//            val button=Button(this)
//            windowManager.addView(button,WindowManager.LayoutParams())
//            button.setOnClickListener{
//                button.text="${Thread.currentThread().name}:${SystemClock.uptimeMillis()}"
//            }
//            Looper.loop()
//        }

//        val textView = findViewById<TextView>(R.id.textView)
//        textView.setOnClickListener {
//            it.requestLayout()
//            thread {
//                textView.text="1234"
//            }
//        }

//        val textView = findViewById<TextView>(R.id.textView)
//        textView.setOnClickListener {
//            textView.text="222"
//            thread {
//                textView.text="1234"
//            }
//        }

//        val textView = findViewById<TextView>(R.id.textView)
//        textView.setOnClickListener {
//            thread {
//                textView.text="1234"
//            }
//        }


    }
}