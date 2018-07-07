package com.example.lianqy.doubleduck_android.ui.Login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lianqy.doubleduck_android.R;
import com.example.lianqy.doubleduck_android.model.LoginState;
import com.example.lianqy.doubleduck_android.model.Saler;
import com.example.lianqy.doubleduck_android.service.LoginService;
import com.example.lianqy.doubleduck_android.ui.Transfer.TransferActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
        //传入餐厅名字
        startActivity(intent);
    }

    private void login() {

        String acc = account_text.getText().toString();
        String pass = password_text.getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.18.218.192:9090/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LoginService service = retrofit.create(LoginService.class);

        if (acc.isEmpty()) {
            Toast.makeText(getApplicationContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
        }
        if (pass.isEmpty() && !acc.isEmpty()) {
            Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
        }

        if (!acc.isEmpty() && !pass.isEmpty()) {
            Call<LoginState> loginCall = service.getLoginState(new Saler(acc, pass, "none"));
            loginCall.enqueue(new Callback<LoginState>() {
                @Override
                public void onResponse(Call<LoginState> call, Response<LoginState> response) {
                    LoginState state = response.body();
                    if (state.getState().equals("noexit")) {
                        Toast.makeText(getApplicationContext(), "该用户不存在", Toast.LENGTH_SHORT).show();
                    } else if (state.getState().equals("Fail")){
                        Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("output", state.getState());
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, TransferActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<LoginState> call, Throwable t) {

                }
            });
        }
    }
}
