package com.example.lianqy.doubleduck_android.ui.ManageDishes;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lianqy.doubleduck_android.R;
import com.example.lianqy.doubleduck_android.model.Dish;
import com.example.lianqy.doubleduck_android.model.Type;
import com.example.lianqy.doubleduck_android.util.BitmapUtil;

import java.util.ArrayList;
import java.util.List;

public class ManageDishesActivity extends AppCompatActivity{

    private static final int RESULT = 1;

    //从相册选取的图片的byte array
    private byte[] byteArray;

    private RecyclerView typeRV, dishRV;
    private ManageTypeAdapter mManageTypeAdapter;
    private ManageDishAdapter mManageDishAdapter;
    private List<Type> mTypeList = new ArrayList<>();
    private List<Dish> mDishList = new ArrayList<>();
    private FloatingActionButton addTypeBtn, addDishBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_dishes);

        initTestData();
        setViews();
        setClick();
        setTypeList();
        setDishist();
    }

    private void setDishist() {
        dishRV.setLayoutManager(new LinearLayoutManager(this));
        mManageDishAdapter = new ManageDishAdapter(mDishList, null);

        //点击事件
        mManageDishAdapter.setOnItemClickListener(new ManageDishAdapter.OnItemClickListener() {
            @Override
            public void onClick(final int position) {
                //短按查看dish详情，并且可以修改
                //对话框
                LayoutInflater factor = LayoutInflater.from(ManageDishesActivity.this);
                final View dishShortClickDialog = factor.inflate(R.layout.dish_short_click_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(ManageDishesActivity.this);
                builder.setView(dishShortClickDialog);

                final EditText name, price, des;
                name = dishShortClickDialog.findViewById(R.id.name);
                price = dishShortClickDialog.findViewById(R.id.price);
                des = dishShortClickDialog.findViewById(R.id.des);
                final ImageView icon = dishShortClickDialog.findViewById(R.id.icon);

                final Dish d = mDishList.get(position);

                name.setText(d.getName());
                price.setText(d.getPrice());
                des.setText(d.getDes());
                icon.setImageBitmap(BitmapUtil.byteArrayToBitmap(d.getSrc()));

                //点击icon直接转到手机相册界面选取图片作为菜品的新icon
                icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, RESULT);
                        //有一个问题就是点击该图片origin之后打开相册选择图片new
                        //如果将origin赋值为图片New,会出现闪退的情况
                        //但是注销此句（即不将new赋值），点击更改后adapter会更新dish的图片为new
                        //icon.setImageBitmap(BitmapUtil.byteArrayToBitmap(byteArray));
                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("更改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        d.setName(name.getText().toString());
                        d.setPrice(price.getText().toString());
                        d.setDes(des.getText().toString());
                        d.setSrc(byteArray == null ? d.getSrc() : byteArray);

                        mDishList.set(position, d);
                        dishRV.getAdapter().notifyItemChanged(position);
                        dialog.dismiss();
                    }
                });

                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

            @Override
            public void onLongClick(int position) {
                //长按删除该dish
                mDishList.remove(position);
                dishRV.getAdapter().notifyItemRemoved(position);
            }
        });

        dishRV.setAdapter(mManageDishAdapter);
    }

    private void setTypeList() {
        typeRV.setLayoutManager(new LinearLayoutManager(this));
        mManageTypeAdapter = new ManageTypeAdapter(mTypeList, null);

        //点击事件
        mManageTypeAdapter.setOnItemClickListener(new ManageTypeAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                //短按右侧的recyclerview显示该类的dishes
                //默认显示第一个类标的dishes
                mDishList = mTypeList.get(position).getDishes();
                mManageDishAdapter = new ManageDishAdapter(mDishList, null);
                dishRV.setAdapter(mManageDishAdapter);
            }

            @Override
            public void onLongClick(final int position) {

                //长按选择更改type name，或者删除此类标
                LayoutInflater factor = LayoutInflater.from(ManageDishesActivity.this);
                final View typeLongClickDialog = factor.inflate(R.layout.type_long_click_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(ManageDishesActivity.this);
                builder.setView(typeLongClickDialog);

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Type t = mTypeList.get(position);
                        mTypeList.remove(position);
                        typeRV.getAdapter().notifyItemRemoved(position);
                    }
                });

                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                typeLongClickDialog.findViewById(R.id.resetTypeName).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //获取新的type name
                        EditText et = typeLongClickDialog.findViewById(R.id.editTypeName);
                        String name = et.getText().toString();

                        Type t = mTypeList.get(position);
                        t.setType(name);
                        mTypeList.set(position, t);
                        typeRV.getAdapter().notifyItemChanged(position);

                        alertDialog.dismiss();
                    }
                });

            }
        });

        typeRV.setAdapter(mManageTypeAdapter);
    }

    private void setClick() {
        addDishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //增加新的dish
                //写一个弹出的对话框吧
            }
        });

        addTypeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //增加新的type
                //直接一个对话框只包含一个text就行吧
            }
        });
    }

    private void setViews() {
        typeRV = findViewById(R.id.typeListRV);
        dishRV = findViewById(R.id.dishListRV);

        addTypeBtn = findViewById(R.id.addTypeFloatingBtn);
        addDishBtn = findViewById(R.id.addDishFloatingBtn);
    }

    private void initTestData() {
        ArrayList<Dish> dishes1 = new ArrayList<>();
        ArrayList<Dish> dishes2 = new ArrayList<>();
        ArrayList<Dish> dishes3 = new ArrayList<>();

        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_password);
        byte[] array = BitmapUtil.bitmapToByteArray(bitmap);
        for(int i = 0; i < 10; i ++){
            Dish d = new Dish("t1 " + Integer.toString(i), "¥ " + Integer.toString(i*2),
                    Integer.toString(i/10 + 1), "i am dish " + Integer.toString(i) + "哈哈哈哈啊啊哈哈哈哈哈啊啊哈",
                    array);

            dishes1.add(d);
        }
        for(int i = 0; i < 10; i ++){
            Dish d = new Dish("t2 " + Integer.toString(i), "¥ " + Integer.toString(i*2),
                    Integer.toString(i/10 + 1), "i am dish " + Integer.toString(i) + "哈哈哈哈啊啊哈哈哈哈哈啊啊哈",
                    array);

            dishes2.add(d);
        }
        for(int i = 0; i < 10; i ++){
            Dish d = new Dish("t3 " + Integer.toString(i), "¥ " + Integer.toString(i*2),
                    Integer.toString(i/10 + 1), "i am dish " + Integer.toString(i) + "哈哈哈哈啊啊哈哈哈哈哈啊啊哈",
                    array);

            dishes3.add(d);
        }

        mDishList = dishes1;

        Type type1 = new Type("烧烤类", dishes1);
        Type type2 = new Type("盐焗类", dishes2);
        Type type3 = new Type("油炸类", dishes3);
        mTypeList.add(type1);
        mTypeList.add(type2);
        mTypeList.add(type3);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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

}
