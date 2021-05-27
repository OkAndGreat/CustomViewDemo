package com.example.customview.window


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.customview.R


class adapter : RecyclerView.Adapter<adapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ll = itemView.findViewById<LinearLayout>(R.id.ll_test)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_test, parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(position%2==0){
            holder.ll.setBackgroundColor(Color.BLACK)
        }else{
            holder.ll.setBackgroundColor(Color.BLUE)
        }

    }

    override fun getItemCount(): Int = 8
}