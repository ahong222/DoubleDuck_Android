package com.example.lianqy.doubleduck_android.ui.RetaurantDetail;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
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
import com.example.lianqy.doubleduck_android.ui.Transfer.bus.ChangeSalerInfoBusEvent;
import com.example.lianqy.doubleduck_android.util.BitmapUtil;
import com.example.lianqy.doubleduck_android.utils.Auth;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.lianqy.doubleduck_android.ui.Transfer.TransferActivity.NAME_FROM_TRANSFER;
import static com.example.lianqy.doubleduck_android.util.BitmapUtil.compressImage;

public class RestaurantDetailActivity extends AppCompatActivity {

    private static final int RESULT = 1;

    private ImageView logo;
    private TextView clickToChangeLogo;
    private EditText name, des, loc, phone;
    private TextView cancel, sure;

    private String sname, sdes, sloc, sphone, slogo;

    private byte[] byteArray;
    private String logourl;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        setContentView(R.layout.activity_restaurant_info);

        setViews();
        getInfo();

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
                sdes = temp.getRtdes();
                sloc = temp.getRtloc();
                sphone = temp.getRtphone();
                slogo = temp.getRtlogo();

                name.setText(sname);
                des.setText(sdes);
                loc.setText(sloc);
                phone.setText(sphone);

                Log.d("output", temp.getRtname());
                Log.d("output", temp.getRtloc());
                Log.d("output", slogo);

                String[] sub = slogo.split("/");

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://p8pbukobc.bkt.clouddn.com/")
                        //.addConverterFactory(GsonConverterFactory.create())
                        .build();
                LoginService service = retrofit.create(LoginService.class);
                Call<ResponseBody>getimg = service.Getpic(sub[sub.length - 1]);
                getimg.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d("output", "success");
                        logo.setImageBitmap(BitmapFactory.decodeStream(response.body().byteStream()));
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("output", "fail");
                    }
                });
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
        UploadManager uploadManager = new UploadManager();
        //Configuration cfg = new Configuration();
        String AccessKey = "IHMM2Arh9QKzsA1YUX-r7z0XyGLW8KCkWMV7-Ydg";
        String SecretKey = "YxOfh_tCFteKZ1uHVjbK6stvSEhK8Em7BF66DrZ4";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String key = "icon_" + sdf.format(new Date());
        uploadManager.put(byteArray, key, Auth.create(AccessKey, SecretKey).uploadToken("systemimage"), new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if (info.isOK()) {
                    logourl = "http://p8pbukobc.bkt.clouddn.com/" + key;
                    Log.d("output", "complete: " + key);

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
                    Call<LoginState> postRtinfo = service.Postrt(new Rtinfo(sname, resDes, resLoc, resPhone, logourl));
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

                    byteArray = byteArray == null ? BitmapUtil.getDefaultLogoByteArray(getApplicationContext()): byteArray;

                    EventBus.getDefault().post(new ChangeSalerInfoBusEvent(sname, resDes, resLoc, resPhone, byteArray));

                } else {
                    Log.d("output", "失败");
                    Log.d("output", info.error);
                }
            }
        }, null);

        //还有一个Logo
        //然后有一个商家类
        //设置那个商家的内容

        //将这个商家的内容传递回transfer activity
        // 回调的方式

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
            byteArray = compressImage(bm);

           // byteArray = byteArray == null ? BitmapUtil.getDefaultByteArray(this): byteArray;

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
