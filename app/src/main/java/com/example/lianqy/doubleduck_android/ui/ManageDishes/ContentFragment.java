package com.example.lianqy.doubleduck_android.ui.ManageDishes;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.lianqy.doubleduck_android.R;
import com.example.lianqy.doubleduck_android.model.Dish;
import com.example.lianqy.doubleduck_android.model.LoginState;
import com.example.lianqy.doubleduck_android.model.PostDish;
import com.example.lianqy.doubleduck_android.model.Type;
import com.example.lianqy.doubleduck_android.service.LoginService;
import com.example.lianqy.doubleduck_android.ui.ManageDishes.adapter.ManageDishAdapter;
import com.example.lianqy.doubleduck_android.ui.ManageDishes.dialog.DishShortClickDialog;
import com.example.lianqy.doubleduck_android.util.BitmapUtil;
import com.example.lianqy.doubleduck_android.utils.Auth;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;
import static com.example.lianqy.doubleduck_android.util.BitmapUtil.compressImage;

public class ContentFragment extends Fragment {

    private static final int RESULT = 1;
    public static final String SELECTED_ITEM = "selected_item";
    private View view;
    //获取点击的种类名称
    private RecyclerView dishRV;
    private ManageDishAdapter mManageDishAdapter;
    private Type mType;
    private List<Dish> mDishList = new ArrayList<>();
    private FloatingActionButton addDishBtn;
    private DishShortClickDialog dialog;

    private byte[] byteArray = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bd = getArguments();
        mType = (Type) bd.getSerializable(SELECTED_ITEM);
        mDishList = mType.getDishes();

        view = inflater.inflate(R.layout.fragment_manage_dishes, null);


        setViews();
        setDishRV();
        setClicks();

        return view;

    }

    private void setClicks() {
        addDishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //增加新的dish
                //final DishShortClickDialog dialog = new DishShortClickDialog(getContext(), null);
                dialog = new DishShortClickDialog(getContext(), null);
                dialog.show();
                dialog.setClickListener(new DishShortClickDialog.ClickListenerInterface() {
                    @Override
                    public void doResetOrSure() {
                        //按下确定键增加新的dish

                        UploadManager uploadManager = new UploadManager();
                        String AccessKey = "IHMM2Arh9QKzsA1YUX-r7z0XyGLW8KCkWMV7-Ydg";
                        String SecretKey = "YxOfh_tCFteKZ1uHVjbK6stvSEhK8Em7BF66DrZ4";
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                        String key = "icon_" + sdf.format(new Date());
                        uploadManager.put(byteArray, key, Auth.create(AccessKey, SecretKey).uploadToken("systemimage"), new UpCompletionHandler() {
                            @Override
                            public void complete(String key, ResponseInfo info, JSONObject response) {
                                if (info.isOK()) {
                                    Log.d("output", "上传成功");
                                    String logourl = "http://p8pbukobc.bkt.clouddn.com/" + key;

                                    Dish d = new Dish(dialog.getName(), dialog.getPrice(), mType.getType(), dialog.getDes(), byteArray, 0);

                                    //服务器上传菜品
                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl("http://172.18.218.192:9090/")
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();
                                    LoginService service = retrofit.create(LoginService.class);
                                    Call<LoginState> postdish = service.Postdish(new PostDish(d.getName(), d.getDes(), Float.parseFloat(d.getPrice()),
                                            logourl,  Integer.parseInt("999"), mType.getType(), "RT1"));
                                    postdish.enqueue(new Callback<LoginState>() {
                                        @Override
                                        public void onResponse(Call<LoginState> call, Response<LoginState> response) {
                                            LoginState temp = response.body();
                                            if (temp.getState().equals("UploadDishSuccess")) {
                                                Log.d("output", "上传菜品成功");
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<LoginState> call, Throwable t) {

                                        }
                                    });

                                    if(mDishList == null){
                                        mDishList = new ArrayList<>();
                                    }
                                    mDishList.add(d);
                                    mType.setDishes(mDishList);
                                    dishRV.getAdapter().notifyItemInserted(mDishList.size()-1);
                                    dialog.dismiss();

                                }
                            }
                        }, null);
                    }

                    @Override
                    public void doCancel() {
                        dialog.dismiss();
                    }

                    @Override
                    public void doFetchPhoto() {
                        byteArray = null;
                        //点击icon直接转到手机相册界面选取图片作为菜品的新icon
                        doCallFetchPic();

                        //byte[] defaultByteArray = BitmapUtil.getDefaultByteArray(getContext());
                        //byteArray = byteArray == null ? defaultByteArray : byteArray;

                        //dialog.setSrc(byteArray);
                    }
                });
            }
        });
    }

    private void setDishRV() {
        dishRV.setLayoutManager(new LinearLayoutManager(getContext()));
        mManageDishAdapter = new ManageDishAdapter(mDishList, null);

        //点击事件
        mManageDishAdapter.setOnItemClickListener(new ManageDishAdapter.OnItemClickListener() {
            @Override
            public void onClick(final int position) {
                final Dish d = mDishList.get(position);
                byteArray = null;

                //短按查看dish详情，并且可以修改
                //对话框
                final DishShortClickDialog dialog = new DishShortClickDialog(getContext(), d);
                dialog.show();
                dialog.setClickListener(new DishShortClickDialog.ClickListenerInterface() {
                    @Override
                    public void doResetOrSure() {
                        //修改
                        resetDish();
                        dialog.dismiss();
                    }

                    @Override
                    public void doCancel() {
                        dialog.dismiss();
                    }

                    @Override
                    public void doFetchPhoto() {
                        //点击icon直接转到手机相册界面选取图片作为菜品的新icon
                        doCallFetchPic();
                        //有一个问题就是点击该图片origin之后打开相册选择图片new
                        //如果将origin赋值为图片New,会出现闪退的情况
                        //但是注销此句（即不将new赋值），点击更改后adapter会更新dish的图片为new
                        ImageView icon = dialog.getView().findViewById(R.id.fetchPic);
                        icon.setImageBitmap(BitmapUtil.byteArrayToBitmap(byteArray == null ? d.getSrc() : byteArray));

                        d.setSrc(byteArray == null ? d.getSrc() : byteArray);

                        resetData();
                        dialog.dismiss();
                    }

                    private void resetDish() {
                        d.setName(dialog.getName());
                        d.setPrice(dialog.getPrice());
                        d.setDes(dialog.getDes());
                        d.setSrc(byteArray == null ? d.getSrc() : byteArray);

                        resetData();
                    }

                    private void resetData() {
                        mDishList.set(position, d);
                        //修改相应的type
                        mType.setDishes(mDishList);
                        //重新设置rv
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

    private void doCallFetchPic() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT);
    }

    private void setViews() {
        dishRV = view.findViewById(R.id.dishListRV);

        addDishBtn = view.findViewById(R.id.addDishFloatingBtn);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT && resultCode == RESULT_OK && data != null){

            //从图库中选择图片
            Uri selectedImage = data.getData();
            //String[] filePathColumn = { MediaStore.Images.Media.DATA };

            //Cursor cursor = getActivity().getContentResolver().query(selectedImage,
            //        filePathColumn, null, null, null);
            //cursor.moveToFirst();

            //int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            //String picturePath = cursor.getString(columnIndex);
           // cursor.close();

            //设置图片并将图片的bitmap格式转换成byte[]存储起来
            //Bitmap bm = BitmapFactory.decodeFile(picturePath);
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_add_64);
            try {
                bm = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //photo.setImageBitmap(bm);
            byteArray = compressImage(bm);

            dialog.setSrc(byteArray);
        }
    }
}
