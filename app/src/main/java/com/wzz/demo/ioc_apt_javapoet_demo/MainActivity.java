package com.wzz.demo.ioc_apt_javapoet_demo;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wzz.demo.annotations.BindContentView;
import com.wzz.demo.annotations.BindOnClick;
import com.wzz.demo.annotations.BindOnLongClick;
import com.wzz.demo.annotations.BindViewID;

import androidx.appcompat.app.AppCompatActivity;

@BindContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    @BindViewID(R.id.textView1)
    TextView mTextView1;
    @BindViewID(R.id.textView2)
    TextView mTextView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //通知绑定工具类绑定
        MyButterKnife.bind(this);
    }

    @BindOnClick({R.id.textView1, R.id.textView2})
    public void onTextViewClick(View view) {
        Toast.makeText(this, "onTextViewClick:" + view.hashCode(), Toast.LENGTH_SHORT).show();
    }

    @BindOnLongClick({R.id.textView1, R.id.textView2})
    public void onTextViewLongClick(View view) {
        Toast.makeText(this, "onTextViewLongClick:" + view.hashCode(), Toast.LENGTH_SHORT).show();
    }

}
