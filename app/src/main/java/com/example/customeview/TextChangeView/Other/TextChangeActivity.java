package com.example.customeview.TextChangeView.Other;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.customeview.R;
import com.example.customeview.TextChangeView.Other.changed.ThumbUpView;
import com.example.customeview.TextChangeView.Other.changed.ThumbView;

public class TextChangeActivity extends AppCompatActivity {
    EditText edNum;
    OldThumbUpView oldThumbUpView;
    ThumbUpView newThumbUpView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_change3);
        edNum = findViewById(R.id.ed_num);
        oldThumbUpView = findViewById(R.id.oldThumbUpView);
        newThumbUpView = findViewById(R.id.newThumbUpView);

        oldThumbUpView.setThumbUpClickListener(new OldThumbUpView.ThumbUpClickListener() {
            @Override
            public void thumbUpFinish() {
                Log.d("MainActivity","Old点赞成功");
            }

            @Override
            public void thumbDownFinish() {
                Log.d("MainActivity","Old取消点赞成功");
            }
        });

        newThumbUpView.setThumbUpClickListener(new ThumbView.ThumbUpClickListener() {
            @Override
            public void thumbUpFinish() {
                Log.d("MainActivity","New点赞成功");
            }

            @Override
            public void thumbDownFinish() {
                Log.d("MainActivity","New取消点赞成功");
            }
        });
        //根据回调Toast的显示可以看出，之前的版本虽然结果正确但是会对回调有可能重复调用多次。
    }

    public void setNum(View v) {
        try {
            int num = Integer.valueOf(edNum.getText().toString().trim());
            oldThumbUpView.setCount(num).setThumbUp(false);
            newThumbUpView.setCount(num).setThumbUp(false);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "只能输入整数", Toast.LENGTH_LONG).show();
        }
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}