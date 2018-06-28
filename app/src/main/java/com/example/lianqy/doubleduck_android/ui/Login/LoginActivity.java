package com.example.lianqy.doubleduck_android.ui.Login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lianqy.doubleduck_android.R;
import com.example.lianqy.doubleduck_android.service.LoginService;
import com.example.lianqy.doubleduck_android.ui.ManageDishes.ManageDishesActivity;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

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

        /*Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.18.218.192:9090/v1/salers/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LoginService service = retrofit.create(LoginService.class);

        if (acc.isEmpty()) {
            Toast.makeText(getApplicationContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
        }
        if (pass.isEmpty() && !acc.isEmpty()) {
            Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
        }
        if (acc.equals("123") && pass.equals("123")) {
            //Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_SHORT).show();

            //从服务器获取是否账号密码匹配
            //跳转到菜品管理页面
            //测试直接点击登录跳转到管理界面
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, ManageDishesActivity.class);
            startActivity(intent);
            finish();
        }*/


        //直接点击登录跳转到管理界面
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, ManageDishesActivity.class);
        startActivity(intent);
        finish();
    }
}
