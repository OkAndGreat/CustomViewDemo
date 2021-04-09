package com.example.customeview;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author wzt
 */
public class SlideMenuActivity extends AppCompatActivity {
    private TextView tvMain;
    private TextView tvDelete;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slidemenu);
        initView();
        initListener();
    }

    private void initListener() {
        tvMain.setOnClickListener(v ->
            Toast.makeText(SlideMenuActivity.this,"You clicked Main",Toast.LENGTH_SHORT).show()
        );
        tvDelete.setOnClickListener(v ->
                Toast.makeText(SlideMenuActivity.this,"You clicked Delete",Toast.LENGTH_SHORT).show());
    }

    private void initView() {
        tvMain = (TextView) findViewById(R.id.tv_main);
        tvDelete = (TextView) findViewById(R.id.tv_delete);
    }
}
