package com.example.customview.test

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.customview.R
import kotlinx.android.synthetic.main.item_test.view.*

/**
 * Author by OkAndGreat，Date on 2021/8/2.
 *
 */
class adapter() : RecyclerView.Adapter<adapter.InnerHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder =
        InnerHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_test, parent, false))

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        holder.itemView.tv_test.text = "Author by OkAndGreat，Date on 2021/8/2."
    }

    override fun getItemCount(): Int = 5000

    class InnerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }
}