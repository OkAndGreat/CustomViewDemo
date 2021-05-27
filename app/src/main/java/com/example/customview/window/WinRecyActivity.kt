package com.example.customview.window

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.customview.R

class WinRecyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_win_recy)
        val rv = findViewById<RecyclerView>(R.id.winrecy_rv)
        rv.layoutManager = CustomLayoutManger()
//        val linearLayoutManager = LinearLayoutManager(this)
//        linearLayoutManager.orientation = RecyclerView.VERTICAL
//        rv.layoutManager = linearLayoutManager
        val adapter = adapter()
        rv.adapter = adapter
        val callback = CustomItemTouchHelper()
        val helper = ItemTouchHelper(callback)
        helper.attachToRecyclerView(rv)
    }
}