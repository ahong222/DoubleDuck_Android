package com.example.lianqy.doubleduck_android;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar titleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
        setTitleBar();
    }

    private void init() {
        titleBar = findViewById(R.id.titlebar);
    }

    private void setTitleBar() {
        titleBar.setTitle("注册新用户");
        titleBar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(titleBar);
    }
}
