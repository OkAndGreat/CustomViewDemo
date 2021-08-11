package com.example.customview.CustomLayoutMnager;

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.customview.CustomLayoutMnager.adapter.UniversalAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SwipeCardCallback extends ItemTouchHelper.SimpleCallback {

    private RecyclerView mRv;
    private UniversalAdapter<SwipeCardBean> adapter;
    private List<SwipeCardBean> mDatas;

    public SwipeCardCallback(RecyclerView mRv,
                             UniversalAdapter<SwipeCardBean> adapter, List<SwipeCardBean> mDatas) {
        //方向
        super(0, 15);//1111
        this.mRv = mRv;
        this.adapter = adapter;
        this.mDatas = mDatas;
    }

    //drag
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    // item 滑出去后回调
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        SwipeCardBean remove = mDatas.remove(viewHolder.getLayoutPosition());//第八个
        mDatas.add(0, remove);//放到第一个
        adapter.notifyDataSetChanged();
    }

    // onDraw
    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder, float dX,
                            float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        double maxDistance = recyclerView.getWidth() * 0.5f;
        double distance = Math.sqrt(dX * dX + dY * dY);
        double fraction = distance / maxDistance;

        if (fraction > 1) {
            fraction = 1;
        }

        // 显示的个数  4个
        int itemCount = recyclerView.getChildCount();

        for (int i = 0; i < itemCount; i++) {
            View view = recyclerView.getChildAt(i);

            int level = itemCount - i - 1;

            if (level > 0) {
                if (level < CardConfig.MAX_SHOW_COUNT - 1) {
                    view.setTranslationY((float) (CardConfig.TRANS_Y_GAP * level - fraction * CardConfig.TRANS_Y_GAP));
                    view.setScaleX((float) (1 - CardConfig.SCALE_GAP * level + fraction * CardConfig.SCALE_GAP));
                    view.setScaleY((float) (1 - CardConfig.SCALE_GAP * level + fraction * CardConfig.SCALE_GAP));
                }
            }
        }
    }

//    @Override
//    public void onChildDraw(@NonNull @NotNull Canvas c, @NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//        int itemCount = recyclerView.getChildCount();
//        for (int i = 0; i < itemCount; i++) {
//            View view = recyclerView.getChildAt(i);
//            int level = itemCount - i - 1;
//            view.setTranslationY((float) (CardConfig.TRANS_Y_GAP * level - CardConfig.TRANS_Y_GAP));
//            view.setScaleX((float) (1 - CardConfig.SCALE_GAP * level + CardConfig.SCALE_GAP));
//            view.setScaleY((float) (1 - CardConfig.SCALE_GAP * level + CardConfig.SCALE_GAP));
//        }
//
//    }


//    @Override
//    public long getAnimationDuration(@NonNull RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {
//        return 500;
//    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return 0.2f;
    }
}
