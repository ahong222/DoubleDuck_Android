package com.example.lianqy.doubleduck_android.ui.Login;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lianqy.doubleduck_android.R;
import com.example.lianqy.doubleduck_android.model.LoginState;
import com.example.lianqy.doubleduck_android.model.Saler;
import com.example.lianqy.doubleduck_android.service.LoginService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar titleBar;
    public EditText reg_acc;
    public EditText reg_pass;
    public EditText reg_com_pass;
    public Button reg_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
        setTitleBar();

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reg_register();
            }
        });
    }

    private void init() {
        titleBar = findViewById(R.id.titlebar);
        reg_acc = findViewById(R.id.reg_account);
        reg_pass = findViewById(R.id.reg_password);
        reg_com_pass = findViewById(R.id.reg_confirm_password);
        reg_btn = findViewById(R.id.reg_register);
    }

    private void setTitleBar() {
        titleBar.setTitle("注册新用户");
        titleBar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(titleBar);
    }

    private void reg_register() {
        String reg_acc_text = reg_acc.getText().toString();
        String reg_pass_text = reg_pass.getText().toString();
        String reg_com_pass_text = reg_com_pass.getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.18.218.192:9090/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LoginService service = retrofit.create(LoginService.class);

        if (reg_acc_text.isEmpty()) {
            Toast.makeText(getApplicationContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
        }
        if (reg_pass_text.isEmpty() && !reg_acc_text.isEmpty()) {
            Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
        }
        if (!reg_acc_text.isEmpty() && !reg_pass_text.isEmpty() && reg_com_pass_text.isEmpty()) {
            Toast.makeText(getApplicationContext(), "请确认密码", Toast.LENGTH_SHORT).show();
        }
        if (!reg_acc_text.isEmpty() && !reg_com_pass_text.isEmpty() && !reg_pass_text.isEmpty() && !reg_com_pass_text.equals(reg_pass_text)) {
            Toast.makeText(getApplicationContext(), "请确认两次密码输入一致", Toast.LENGTH_SHORT).show();
        }
        if (!reg_acc_text.isEmpty() && !reg_com_pass_text.isEmpty() && !reg_pass_text.isEmpty() && reg_com_pass_text.equals(reg_pass_text)) {
            Call<LoginState> registerCall = service.getRegisterState(new Saler(reg_acc_text, reg_pass_text, "测试"));
            registerCall.enqueue(new Callback<LoginState>() {
                @Override
                public void onResponse(Call<LoginState> call, Response<LoginState> response) {

                }

                @Override
                public void onFailure(Call<LoginState> call, Throwable t) {

                }
            });
        }
    }
}
