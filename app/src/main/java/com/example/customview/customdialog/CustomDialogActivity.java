package com.example.customview.customdialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.customview.R;

public class CustomDialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_dialog);

        findViewById(R.id.button2).setOnClickListener(
                v -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    LayoutInflater inflater = LayoutInflater.from(this);
                    View view = inflater.inflate(R.layout.custom_dialog, null);
                    TextView content = (TextView) view.findViewById(R.id.tv_custom_dialog);
                    //builer.setView(v);//这里如果使用builer.setView(v)，自定义布局只会覆盖title和button之间的那部分
                    final Dialog dialog = builder.create();
                    dialog.show();
                    dialog.getWindow().setContentView(view);//自定义布局应该在这里添加，要在dialog.show()的后面
                    //dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
                    content.setOnClickListener(
                            vi -> {
                                Toast.makeText(this, "哈哈哈", Toast.LENGTH_LONG).show();
                            }
                    );
                }
        );


    }
}