package com.example.lianqy.doubleduck_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public Button register_btn;
    public Button login_btn;
    public EditText account_text;
    public EditText password_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void init() {
        register_btn = findViewById(R.id.register);
        login_btn = findViewById(R.id.login);
        account_text = findViewById(R.id.account);
        password_text = findViewById(R.id.password);
    }

    public void register() {
        //跳转到注册页面
        Intent intent = new Intent();
        intent.setClass(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void login() {
        String acc = account_text.getText().toString();
        String pass = password_text.getText().toString();
        if (acc.isEmpty()) {
            Toast.makeText(getApplicationContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
        }
        if (pass.isEmpty() && !acc.isEmpty()) {
            Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
        }
        if (acc.equals("123") && pass.equals("123")) {
            Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_SHORT).show();
        }
    }
}
