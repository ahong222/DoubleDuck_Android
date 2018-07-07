package com.example.lianqy.doubleduck_android.ui.ManageDishes;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import com.example.lianqy.doubleduck_android.R;
import com.example.lianqy.doubleduck_android.model.AllDish;
import com.example.lianqy.doubleduck_android.model.AllDishes;
import com.example.lianqy.doubleduck_android.model.Dish;
import com.example.lianqy.doubleduck_android.model.LoginState;
import com.example.lianqy.doubleduck_android.model.Type;
import com.example.lianqy.doubleduck_android.model.postcate;
import com.example.lianqy.doubleduck_android.service.LoginService;
import com.example.lianqy.doubleduck_android.ui.ManageDishes.adapter.DrawerAdapter;
import com.example.lianqy.doubleduck_android.ui.ManageDishes.dialog.TypeLongClickDialog;
import com.example.lianqy.doubleduck_android.util.BitmapUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.lianqy.doubleduck_android.ui.Transfer.TransferActivity.NAME_FROM_TRANSFER;

public class ManageDishesActivity extends AppCompatActivity{
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ListView menuDrawer;
    private DrawerAdapter mAdapter;
    private String contentTitle;
    private ActionBarDrawerToggle mDrawerToggle;

    private List<Type> mTypeList = new ArrayList<>();

    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_dishes);

        initTypesAndDishes();

        getInfo();

        getdishes();

        /*初始化View*/
        initViews();

        /*设置ActionBar*/
        setActionBar();

        /*设置Drawerlayout开关*/
        setDrawerToggle();

    }

    private void getInfo() {
        Intent i = getIntent();
        name = i.getStringExtra(NAME_FROM_TRANSFER);
    }

    private void getdishes() {
        //获取菜品
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.18.218.192:9090/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LoginService service = retrofit.create(LoginService.class);
        Call<AllDishes> getdish = service.Getdishes(name);
        getdish.enqueue(new Callback<AllDishes>() {
            @Override
            public void onResponse(Call<AllDishes> call, Response<AllDishes> response) {
                AllDishes temp = response.body();
                List<AllDish> dish = temp.getAlldishes();  //AllDish是一个种类   dish种类列表
                //现在将这个种类列表转化成typeList
                //emmm....

                Log.d("output", String.valueOf(dish.size()));
                for (int i = 0; i < dish.size(); i ++) {
                    Log.d("output", dish.get(i).getCategory());
                }
            }

            @Override
            public void onFailure(Call<AllDishes> call, Throwable t) {
                Log.d("output", "失败了");
                Log.d("output", t.getMessage());
            }
        });
    }

    /*初始化View*/
    private void initViews() {
        contentTitle = "菜品管理";

        menuDrawer = findViewById(R.id.left_fragment);
        mAdapter = new DrawerAdapter(this, mTypeList);
        menuDrawer.setAdapter(mAdapter);
        menuDrawer.setOnItemClickListener(new DrawerItemClickListener());
        menuDrawer.setOnItemLongClickListener(new DrawerItemLongClickListener());

        mToolbar = findViewById(R.id.toolbar);

        mDrawerLayout = findViewById(R.id.drawer_layout);
    }

    /*设置ActionBar*/
    private void setActionBar() {
        setSupportActionBar(mToolbar);

        //设置ActionBar的指示图标可见，设置ActionBar上的应用图标位置处可以被单击
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(contentTitle);
        //隐藏ActionBar上的应用图标，只显示文字label
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    /*设置Drawerlayout的开关,并且和Home图标联动*/
    private void setDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, 0, 0);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        /*同步drawerlayout的状态*/
        mDrawerToggle.syncState();
    }


    public void setTitle(String title){
        contentTitle = title;
        getSupportActionBar().setTitle(title);
    }

    //开启frament界面并且传递参数
    public void selectItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ContentFragment.SELECTED_ITEM, mAdapter.getType(position));
        Fragment contentFragment = new ContentFragment();
        contentFragment.setArguments(bundle);

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content_frame, contentFragment).commit();

        menuDrawer.setItemChecked(position, true);
        setTitle(mAdapter.getItem(position).title);
        mDrawerLayout.closeDrawer(menuDrawer);
    }

    public class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    //长按弹出对话框选择删除或者更改type
    public class DrawerItemLongClickListener implements ListView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            //长按选择更改type name，或者删除此类标
            final Type t = mTypeList.get(position);
            final TypeLongClickDialog dialog = new TypeLongClickDialog(ManageDishesActivity.this,t);
            dialog.show();
            dialog.setClickListener(new TypeLongClickDialog.ClickListenerInterface() {
                @Override
                public void doReset() {
                    //获取新的type name
                    t.setType(dialog.getTypeName());
                    mTypeList.set(position, t);

                    resetAdapter();
                    setTitle(mAdapter.getItem(position).title);

                    dialog.dismiss();
                }

                @Override
                public void doCancel() {
                    dialog.dismiss();
                }

                @Override
                public void doDeleteOrSure() {
                    mTypeList.remove(position);
                    resetAdapter();
                    setTitle("菜品管理");

                    dialog.dismiss();
                    mDrawerLayout.closeDrawer(menuDrawer);
                }
            });

            return false;
        }
    }

    private void resetAdapter() {
        mAdapter = new DrawerAdapter(ManageDishesActivity.this, mTypeList);
        menuDrawer.setAdapter(mAdapter);
    }

    public class DrawerMenuToggle extends ActionBarDrawerToggle {
        public DrawerMenuToggle(Activity activity, DrawerLayout drawerLayout, int drawer_open, int drawer_close) {
            super(activity, drawerLayout, drawer_open, drawer_close);
        }
        /** 当侧滑菜单达到完全关闭的状态时，回调这个方法 */
         public void onDrawerClosed(View view) {
             super.onDrawerClosed(view);
           //当侧滑菜单关闭后，显示ListView选中项的标题，如果并没有点击ListView中的任何项，那么显示原来的标题
             getSupportActionBar().setTitle(contentTitle);
             invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
         }

         /** 当侧滑菜单完全打开时，这个方法被回调 */
         public void onDrawerOpened(View drawerView) {
             super.onDrawerOpened(drawerView);
             getSupportActionBar().setTitle("菜品管理"); //当侧滑菜单打开时ActionBar显示全局标题
             invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
         }
    }

    /**为了能够让ActionBarDrawerToggle监听器
      * 能够在Activity的整个生命周期中都能够以正确的逻辑工作
      * 需要添加下面两个方法*/
     @Override
     protected void onPostCreate(Bundle savedInstanceState) {
         super.onPostCreate(savedInstanceState);
         // Sync the toggle state after onRestoreInstanceState has occurred.
         mDrawerToggle.syncState();
     }

     @Override
     public void onConfigurationChanged(Configuration newConfig) {
         super.onConfigurationChanged(newConfig);
         mDrawerToggle.onConfigurationChanged(newConfig);
     }

     /**最后做一些菜单上处理*/
     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         // Inflate the menu; this adds items to the action bar if it is present.
         getMenuInflater().inflate(R.menu.navigation, menu);
         return true;
     }

     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
         // Handle action bar item clicks here. The action bar will
         // automatically handle clicks on the Home/Up button, so long
         // as you specify a parent activity in AndroidManifest.xml.
         int id = item.getItemId();
         //第一个if 要加上，为的是让ActionBarDrawerToggle以正常的逻辑工作
         if (mDrawerToggle.onOptionsItemSelected(item)) {
               return true;
         }
         if (id == R.id.action_addType) {
             //增加新的种类
             final TypeLongClickDialog dialog = new TypeLongClickDialog(ManageDishesActivity.this, null);
             dialog.show();
             dialog.setClickListener(new TypeLongClickDialog.ClickListenerInterface() {
                 @Override
                 public void doReset() {
                     //没有
                 }

                 @Override
                 public void doCancel() {
                     dialog.dismiss();
                 }

                 @Override
                 public void doDeleteOrSure() {
                     //确定增加新的种类
                     Type t = new Type(dialog.getTypeName(), null);
                     mTypeList.add(t);
                     uploadcate(t);
                     resetAdapter();

                     dialog.dismiss();
                 }
             });
             return true;
         }

         return super.onOptionsItemSelected(item);
     }

     //上传菜品种类
     private void uploadcate(Type t) {
         Retrofit retrofit = new Retrofit.Builder()
                 .baseUrl("http://172.18.218.192:9090/")
                 .addConverterFactory(GsonConverterFactory.create())
                 .build();
         LoginService service = retrofit.create(LoginService.class);
         Call<LoginState> upcate = service.Postcate(new postcate(name, t.getType()));
         upcate.enqueue(new Callback<LoginState>() {
             @Override
             public void onResponse(Call<LoginState> call, Response<LoginState> response) {
                 LoginState temp = response.body();
                 if (temp.getState().equals("createCateSuccess")) {
                     Log.d("output", "成功创建上传");
                 }
             }

             @Override
             public void onFailure(Call<LoginState> call, Throwable t) {

             }
         });
     }


     /**每次调用 invalidateOptionsMenu() ，下面的这个方法就会被回调*/
     @Override
     public boolean onPrepareOptionsMenu(Menu menu) {

         // 如果侧滑菜单的状态监听器在侧滑菜单打开和关闭时都调用了invalidateOptionsMenu()方法，
         //当侧滑菜单打开时将ActionBar上的某些菜单图标隐藏起来，使得这时仅显示“推酷”这个全局标题
         //本应用中是将ActiongBar上的action菜单项隐藏起来

         boolean drawerOpen = mDrawerLayout.isDrawerOpen(menuDrawer);//判定当前侧滑菜单的状态
         menu.findItem(R.id.action_addType).setVisible(!drawerOpen);
         return super.onPrepareOptionsMenu(menu);
     }

     /**当用户按下了"手机上的返回功能按键"的时候会回调这个方法*/
     @Override
     public void onBackPressed() {
         boolean drawerState =  mDrawerLayout.isDrawerOpen(menuDrawer);
         if (drawerState) {
             mDrawerLayout.closeDrawers();
             return;
         }
         //也就是说，当按下返回功能键的时候，不是直接对Activity进行弹栈，而是先将菜单视图关闭
         super.onBackPressed();
     }


    private void initTypesAndDishes() {
        ArrayList<Dish> dishesShaokao = new ArrayList<>();
        ArrayList<Dish> dishesYinliao = new ArrayList<>();
        ArrayList<Dish> dishesRexiao = new ArrayList<>();

        Bitmap bitmap1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.kao_ya_bo);
        byte[] array1 = BitmapUtil.bitmapToByteArray(bitmap1);
        Dish d11 = new Dish("烤鸭脖", "¥14", "烧烤类","美味鸭脖！！！", array1);
        dishesShaokao.add(d11);

        Bitmap bitmap2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.kao_ya_chi);
        byte[] array2 = BitmapUtil.bitmapToByteArray(bitmap2);
        Dish d12 = new Dish("烤鸭翅", "¥13", "烧烤类","美味鸭翅！！！", array2);
        dishesShaokao.add(d12);

        Bitmap bitmap3 = BitmapFactory.decodeResource(this.getResources(), R.drawable.kao_ya_tui);
        byte[] array3 = BitmapUtil.bitmapToByteArray(bitmap3);
        Dish d13 = new Dish("烤鸭腿", "¥12", "烧烤类","美味鸭腿！！！", array3);
        dishesShaokao.add(d13);

        Bitmap bitmap4 = BitmapFactory.decodeResource(this.getResources(), R.drawable.bing_ke_le);
        byte[] array4 = BitmapUtil.bitmapToByteArray(bitmap4);
        Dish d21 = new Dish("冰可乐", "¥3", "饮料类","透心凉！心飞扬", array4);
        dishesYinliao.add(d21);

        Bitmap bitmap5 = BitmapFactory.decodeResource(this.getResources(), R.drawable.bing_xue_bi);
        byte[] array5 = BitmapUtil.bitmapToByteArray(bitmap5);
        Dish d22 = new Dish("冰雪碧", "¥3", "饮料类","透心凉！还加了膳食纤维哦~", array5);
        dishesYinliao.add(d22);

        Bitmap bitmap6 = BitmapFactory.decodeResource(this.getResources(), R.drawable.qing_zheng_shuang_ya);
        byte[] array6 = BitmapUtil.bitmapToByteArray(bitmap6);
        Dish d31 = new Dish("清蒸双鸭", "¥18", "热销","美味清蒸双鸭！！！", array6);
        dishesRexiao.add(d31);

        Bitmap bitmap7 = BitmapFactory.decodeResource(this.getResources(), R.drawable.hong_shao_shuang_ya);
        byte[] array7 = BitmapUtil.bitmapToByteArray(bitmap7);
        Dish d32 = new Dish("红烧双鸭", "¥19", "热销","美味红烧双鸭！！！", array7);
        dishesRexiao.add(d32);

        Bitmap bitmap8 = BitmapFactory.decodeResource(this.getResources(), R.drawable.shuang_ya_fei_pian);
        byte[] array8 = BitmapUtil.bitmapToByteArray(bitmap8);
        Dish d33 = new Dish("双鸭肺片", "¥20", "热销","美味双鸭肺片！！！", array6);
        dishesRexiao.add(d31);

        Type type1 = new Type("热销", dishesRexiao);
        Type type2 = new Type("烧烤类", dishesShaokao);
        Type type3 = new Type("饮料类", dishesYinliao);

        //mTypeList.add(type1);
        mTypeList.add(type2);
        mTypeList.add(type3);

    }

}