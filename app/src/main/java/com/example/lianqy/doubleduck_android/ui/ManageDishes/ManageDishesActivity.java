package com.example.lianqy.doubleduck_android.ui.ManageDishes;

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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lianqy.doubleduck_android.R;
import com.example.lianqy.doubleduck_android.model.Dish;
import com.example.lianqy.doubleduck_android.model.Type;
import com.example.lianqy.doubleduck_android.ui.ManageDishes.adapter.ManageDishAdapter;
import com.example.lianqy.doubleduck_android.ui.ManageDishes.adapter.ManageTypeAdapter;
import com.example.lianqy.doubleduck_android.ui.ManageDishes.dialog.DishShortClickDialog;
import com.example.lianqy.doubleduck_android.ui.ManageDishes.dialog.TypeLongClickDialog;
import com.example.lianqy.doubleduck_android.util.BitmapUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.DeflaterInputStream;

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

    //记录最后一次点击的dish/type rv的位置
    private int dishPos = 0, typePos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_dishes);

        initTestData();
        setViews();
        setClick();
        setTypeList();
        setDishist(mDishList);
    }

    private void setDishist(final List<Dish> dishist) {
        dishRV.setLayoutManager(new LinearLayoutManager(this));
        mManageDishAdapter = new ManageDishAdapter(dishist, null);

        //点击事件
        mManageDishAdapter.setOnItemClickListener(new ManageDishAdapter.OnItemClickListener() {
            @Override
            public void onClick(final int position) {

                dishPos = position;
                final Dish d = dishist.get(position);
                byteArray = null;

                //短按查看dish详情，并且可以修改
                //对话框
                final DishShortClickDialog dialog = new DishShortClickDialog(ManageDishesActivity.this, d);
                dialog.show();
                dialog.setClickListener(new DishShortClickDialog.ClickListenerInterface() {
                    @Override
                    public void doResetOrSure() {
                        d.setName(dialog.getName());
                        d.setPrice(dialog.getPrice());
                        d.setDes(dialog.getDes());
                        d.setSrc(byteArray == null ? d.getSrc() : byteArray);

                        dishist.set(position, d);
                        //设置mDishList的同时，要修改库里（服务器）的List
                        //还要修改相应的type的list<Dish>
                        Type t = mTypeList.get(typePos);
                        t.setDishes(dishist);
                        mTypeList.set(typePos, t);
                        dishRV.getAdapter().notifyItemChanged(position);
                        dialog.dismiss();
                    }

                    @Override
                    public void doCancel() {
                        dialog.dismiss();
                    }

                    @Override
                    public void doFetchPhoto() {
                        //点击icon直接转到手机相册界面选取图片作为菜品的新icon
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, RESULT);
                        //有一个问题就是点击该图片origin之后打开相册选择图片new
                        //如果将origin赋值为图片New,会出现闪退的情况
                        //但是注销此句（即不将new赋值），点击更改后adapter会更新dish的图片为new
                        ImageView icon = dialog.getView().findViewById(R.id.fetchPic);
                        icon.setImageBitmap(BitmapUtil.byteArrayToBitmap(byteArray == null ? d.getSrc() : byteArray));

                        d.setSrc(byteArray == null ? d.getSrc() : byteArray);

                        dishist.set(position, d);
                        //设置mDishList的同时，要修改库里（服务器）的List
                        //还要修改相应的type的list<Dish>
                        Type t = mTypeList.get(typePos);
                        t.setDishes(dishist);
                        mTypeList.set(typePos, t);
                        dishRV.getAdapter().notifyItemChanged(position);
                    }
                });

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
                typePos = position;

                //短按右侧的recyclerview显示该类的dishes
                //默认显示第一个类标的dishes
                /*
                mDishList = mTypeList.get(position).getDishes();
                mManageDishAdapter = new ManageDishAdapter(mDishList, null);
                dishRV.setAdapter(mManageDishAdapter);
                */
                mDishList = mTypeList.get(position).getDishes();
                if(mDishList == null){
                    mDishList = new ArrayList<>();
                }
                setDishist(mDishList);
            }

            @Override
            public void onLongClick(final int position) {

                //长按选择更改type name，或者删除此类标
                final Type t = mTypeList.get(position);
                final TypeLongClickDialog dialog = new TypeLongClickDialog(ManageDishesActivity.this, t);
                dialog.show();
                dialog.setClickListener(new TypeLongClickDialog.ClickListenerInterface() {
                    @Override
                    public void doReset() {
                        //获取新的type name
                        t.setType(dialog.getTypeName());
                        mTypeList.set(position, t);
                        typeRV.getAdapter().notifyItemChanged(position);

                        dialog.dismiss();
                    }

                    @Override
                    public void doCancel() {
                        dialog.dismiss();
                    }

                    @Override
                    public void doDeleteOrSure() {
                        mTypeList.remove(position);
                        typeRV.getAdapter().notifyItemRemoved(position);
                        dialog.dismiss();
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
                final DishShortClickDialog dialog = new DishShortClickDialog(ManageDishesActivity.this, null);
                dialog.show();
                dialog.setClickListener(new DishShortClickDialog.ClickListenerInterface() {
                    @Override
                    public void doResetOrSure() {
                        //按下确定键增加新的dish
                        String tName = mTypeList.get(typePos).getType();
                        Dish d = new Dish(dialog.getName(), dialog.getPrice(), tName, dialog.getDes(), dialog.getSrc());

                        mDishList.add(d);
                        mTypeList.get(typePos).setDishes(mDishList);
                        dishRV.getAdapter().notifyItemInserted(mDishList.size()-1);
                        dialog.dismiss();
                    }

                    @Override
                    public void doCancel() {
                        dialog.dismiss();
                    }

                    @Override
                    public void doFetchPhoto() {

                    }
                });
            }
        });

        addTypeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //增加新的type
                final TypeLongClickDialog dialog = new TypeLongClickDialog(ManageDishesActivity.this, null);
                dialog.show();
                dialog.setClickListener(new TypeLongClickDialog.ClickListenerInterface() {
                    @Override
                    public void doReset() {
                        //已经隐藏，不管
                    }

                    @Override
                    public void doCancel() {
                        dialog.dismiss();
                    }

                    @Override
                    public void doDeleteOrSure() {
                        //确定新增一个type
                        Type t = new Type(dialog.getTypeName(), null);
                        mTypeList.add(t);
                        typeRV.getAdapter().notifyItemInserted(mTypeList.size() - 1);
                        dialog.dismiss();
                    }
                });
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

        Bitmap bitmap = BitmapFactory.decodeResource(ManageDishesActivity.this.getResources(), R.drawable.ic_happy_64);
        byte[] array = BitmapUtil.bitmapToByteArray(bitmap);
        for (int i = 0; i < 10; i++) {
            Dish d = new Dish("t1 " + Integer.toString(i), "¥ " + Integer.toString(i * 2),
                    Integer.toString(i / 10 + 1), "i am dish " + Integer.toString(i) + "哈哈哈哈啊啊哈哈哈哈哈啊啊哈",
                    array);

            dishes1.add(d);
        }
        for (int i = 0; i < 10; i++) {
            Dish d = new Dish("t2 " + Integer.toString(i), "¥ " + Integer.toString(i * 2),
                    Integer.toString(i / 10 + 1), "i am dish " + Integer.toString(i) + "哈哈哈哈啊啊哈哈哈哈哈啊啊哈",
                    array);

            dishes2.add(d);
        }
        for (int i = 0; i < 10; i++) {
            Dish d = new Dish("t3 " + Integer.toString(i), "¥ " + Integer.toString(i * 2),
                    Integer.toString(i / 10 + 1), "i am dish " + Integer.toString(i) + "哈哈哈哈啊啊哈哈哈哈哈啊啊哈",
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

    //点击返回键结束该activity
    @Override
    public void onBackPressed(){
        finish();
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