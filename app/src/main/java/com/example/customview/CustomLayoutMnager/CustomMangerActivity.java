package com.example.customview.CustomLayoutMnager;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;


import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.customview.CustomLayoutMnager.adapter.UniversalAdapter;
import com.example.customview.CustomLayoutMnager.adapter.ViewHolder;
import com.example.customview.R;

import java.util.List;

public class CustomMangerActivity extends Activity {

    private RecyclerView rv;
    private UniversalAdapter<SwipeCardBean> adapter;
    private List<SwipeCardBean> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_card);

        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new SwipeCardLayoutManager());

        mDatas = SwipeCardBean.initDatas();
        adapter = new UniversalAdapter<SwipeCardBean>(CustomMangerActivity.this, mDatas, R.layout.item_swipe_card) {
            @Override
            public void convert(ViewHolder ViewHolder, SwipeCardBean swipeCardBean) {
                ViewHolder.setText(R.id.tvName, swipeCardBean.getName());
                ViewHolder.setText(R.id.tvPrecent, swipeCardBean.getPostition() + "/" + mDatas.size());
                Glide.with(CustomMangerActivity.this)
                        .load(swipeCardBean.getUrl())
                        .into((ImageView) ViewHolder.getView(R.id.iv));
            }
        };
        rv.setAdapter(adapter);
        CardConfig.initConfig(this);

        ItemTouchHelper.Callback callback = new SwipeCardCallback(rv, adapter, mDatas);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(rv);

    }

}
