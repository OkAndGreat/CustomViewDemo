package com.example.customeview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;

import com.example.customeview.custome.Bezier3;
import com.example.customeview.custome.MagicCircle;

public class BesaierActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btn6;
    private Button btn7;
    private Button btn8;
    private Button btn9;
    private Button btn10;
    private Button btn11;
    private Button btn12;
    private Bezier3 bezier3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_besaier);
        btn1 = (Button) findViewById(R.id.btn_1);
        btn2 = (Button) findViewById(R.id.btn_2);
        btn3 = (Button) findViewById(R.id.btn_3);
        btn4 = (Button) findViewById(R.id.btn_4);
        btn5 = (Button) findViewById(R.id.btn_5);
        btn6 = (Button) findViewById(R.id.btn_6);
        btn7 = (Button) findViewById(R.id.btn_7);
        btn8 = (Button) findViewById(R.id.btn_8);
        btn9 = (Button) findViewById(R.id.btn_9);
        btn10 = (Button) findViewById(R.id.btn_10);
        btn11 = (Button) findViewById(R.id.btn_11);
        btn12 = (Button) findViewById(R.id.btn_12);
        bezier3 = findViewById(R.id.Bezier3);
        ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 1.4f, 1.0f,
                1.4f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnim.setRepeatCount(Animation.INFINITE);
        scaleAnim.setRepeatMode(Animation.REVERSE);
        scaleAnim.setDuration(700);
        bezier3.startAnimation(scaleAnim);
        initListener();
    }

    private void initListener() {
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btn10.setOnClickListener(this);
        btn11.setOnClickListener(this);
        btn12.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_1) {
            bezier3.setMode(1);
        } else if (id == R.id.btn_2) {
            bezier3.setMode(2);
        } else if (id == R.id.btn_3) {
            bezier3.setMode(3);
        } else if (id == R.id.btn_4) {
            bezier3.setMode(4);
        } else if (id == R.id.btn_5) {
            bezier3.setMode(5);
        } else if (id == R.id.btn_6) {
            bezier3.setMode(6);
        } else if (id == R.id.btn_7) {
            bezier3.setMode(7);
        } else if (id == R.id.btn_8) {
            bezier3.setMode(8);
        } else if (id == R.id.btn_9) {
            bezier3.setMode(9);
        } else if (id == R.id.btn_10) {
            bezier3.setMode(10);
        } else if (id == R.id.btn_11) {
            bezier3.setMode(11);
        } else if (id == R.id.btn_12) {
            bezier3.setMode(12);
        }
    }
}