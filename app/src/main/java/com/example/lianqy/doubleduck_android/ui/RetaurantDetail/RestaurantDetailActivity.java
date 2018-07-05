package com.example.lianqy.doubleduck_android.ui.RetaurantDetail;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lianqy.doubleduck_android.R;
import com.example.lianqy.doubleduck_android.ui.Transfer.bus.ChangeSalerInfoBusEvent;
import com.example.lianqy.doubleduck_android.util.BitmapUtil;

import org.greenrobot.eventbus.EventBus;

public class RestaurantDetailActivity extends AppCompatActivity {

    private static final int RESULT = 1;

    private ImageView logo;
    private TextView clickToChangeLogo;
    private EditText name, des;
    private TextView cancel, sure;

    private byte[] byteArray;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        setContentView(R.layout.activity_restaurant_info);

        setViews();
        setClicks();
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
        //还有一个Logo
        //然后有一个商家类
        //设置那个商家的内容

        //将这个商家的内容传递回transfer activity
        // 回调的方式
        byteArray = byteArray == null ? BitmapUtil.getDefaultLogoByteArray(this): byteArray;

        EventBus.getDefault().post(new ChangeSalerInfoBusEvent(resName, resDes, byteArray));
    }

    private void fetchPicAndReplace() {
        byteArray = null;
        doCallFetchPic();
        byteArray = byteArray == null ? BitmapUtil.getDefaultByteArray(this): byteArray;

        //更换logo内容
        logo.setImageBitmap(BitmapUtil.byteArrayToBitmap(byteArray));
    }

    private void doCallFetchPic() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT);
    }

    private void setViews() {
        logo = findViewById(R.id.Logo);
        name = findViewById(R.id.Name);
        des = findViewById(R.id.Des);
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
            Bitmap bm = BitmapFactory.decodeFile(picturePath);
            //photo.setImageBitmap(bm);
            byteArray = BitmapUtil.bitmapToByteArray(bm);
        }
    }

    //点击返回键结束该activity
    @Override
    public void onBackPressed(){
        finish();
    }

}
