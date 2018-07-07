package com.example.lianqy.doubleduck_android.ui.RetaurantDetail;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lianqy.doubleduck_android.R;
import com.example.lianqy.doubleduck_android.model.LoginState;
import com.example.lianqy.doubleduck_android.model.Rtinfo;
import com.example.lianqy.doubleduck_android.service.LoginService;
import com.example.lianqy.doubleduck_android.ui.Statistics.ResStatisticsActivity;
import com.example.lianqy.doubleduck_android.ui.Transfer.bus.ChangeSalerInfoBusEvent;
import com.example.lianqy.doubleduck_android.util.BitmapUtil;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.lianqy.doubleduck_android.ui.Transfer.TransferActivity.NAME_FROM_TRANSFER;

public class RestaurantDetailActivity extends AppCompatActivity {

    private static final int RESULT = 1;

    private ImageView logo;
    private TextView clickToChangeLogo;
    private EditText name, des, loc, phone;
    private TextView cancel, sure;

    private String sname, sdes, sloc, sphone;

    private byte[] byteArray;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        setContentView(R.layout.activity_restaurant_info);

        getInfo();

        setViews();
        setClicks();
    }

    private void getInfo() {
        Intent i = getIntent();
        sname = i.getStringExtra(NAME_FROM_TRANSFER);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.18.218.192:9090/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
                LoginService service = retrofit.create(LoginService.class);
        Call<Rtinfo> getrtinfo = service.getRtInfo(sname);
        getrtinfo.enqueue(new Callback<Rtinfo>() {
            @Override
            public void onResponse(Call<Rtinfo> call, Response<Rtinfo> response) {
                Rtinfo temp = response.body();
                //RtInfoRTname = temp.getRtname();
                sdes = temp.getRtdes();
                sloc = temp.getRtloc();
                sphone = temp.getRtphone();

                Log.d("output", temp.getRtname());
                Log.d("output", temp.getRtloc());
            }

            @Override
            public void onFailure(Call<Rtinfo> call, Throwable t) {

            }
        });
    }


    private void setClicks() {
        clickToChangeLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchPicAndReplace();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //退出这个界面
                finish();
            }
        });

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeRestaurntInfo();
                finish();
            }
        });
    }

    private void changeRestaurntInfo() {
        String resName = name.getText().toString();
        String resDes = des.getText().toString();
        String resLoc = loc.getText().toString();
        String resPhone = phone.getText().toString();

        //更新服务器的饭店信息
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.18.218.192:9090/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LoginService service = retrofit.create(LoginService.class);
        Call<LoginState> postRtinfo = service.Postrt(new Rtinfo(sname, resDes, resLoc, resPhone, "url"));
        postRtinfo.enqueue(new Callback<LoginState>() {
            @Override
            public void onResponse(Call<LoginState> call, Response<LoginState> response) {
                LoginState temp = response.body();
                if (temp.getState().equals("UploadRTInfoSuccess")) {
                    Log.d("output", "更新饭店信息成功");
                }
            }

            @Override
            public void onFailure(Call<LoginState> call, Throwable t) {

            }
        });
        //还有一个Logo
        //然后有一个商家类
        //设置那个商家的内容

        //将这个商家的内容传递回transfer activity
        // 回调的方式
        byteArray = byteArray == null ? BitmapUtil.getDefaultLogoByteArray(this): byteArray;

        EventBus.getDefault().post(new ChangeSalerInfoBusEvent(resName, resDes, resLoc, resPhone, byteArray));
    }

    private void fetchPicAndReplace() {
        byteArray = null;
        doCallFetchPic();
        //byteArray = byteArray == null ? BitmapUtil.getDefaultByteArray(this): byteArray;

        //更换logo内容
        //logo.setImageBitmap(BitmapUtil.byteArrayToBitmap(byteArray));
    }

    private void doCallFetchPic() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT);
    }

    private void setViews() {
        logo = findViewById(R.id.Logo);
        name = findViewById(R.id.Name);
        des = findViewById(R.id.Des);
        loc = findViewById(R.id.Loc);
        phone = findViewById(R.id.Phone);

        name.setText(sname);
        des.setText(sdes);
        loc.setText(sloc);
        phone.setText(sphone);

        clickToChangeLogo = findViewById(R.id.clickChangeLogo);

        cancel = findViewById(R.id.Cancel);
        sure = findViewById(R.id.Sure);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT && resultCode == RESULT_OK && data != null){

            //从图库中选择图片
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            //设置图片并将图片的bitmap格式转换成byte[]存储起来
            //Bitmap bm = BitmapFactory.decodeFile(picturePath);
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_add_64);
            try {
                bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //photo.setImageBitmap(bm);
            byteArray = BitmapUtil.bitmapToByteArray(bm);

            byteArray = byteArray == null ? BitmapUtil.getDefaultByteArray(this): byteArray;

            //更换logo内容
            logo.setImageBitmap(BitmapUtil.byteArrayToBitmap(byteArray));
        }
    }

    //点击返回键结束该activity
    @Override
    public void onBackPressed(){
        finish();
    }

}
