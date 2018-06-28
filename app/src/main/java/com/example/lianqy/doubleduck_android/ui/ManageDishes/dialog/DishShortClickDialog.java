package com.example.lianqy.doubleduck_android.ui.ManageDishes.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lianqy.doubleduck_android.R;
import com.example.lianqy.doubleduck_android.model.Dish;
import com.example.lianqy.doubleduck_android.util.BitmapUtil;

public class DishShortClickDialog extends Dialog {

    private Context mContext;
    private ClickListenerInterface mClickListenerInterface;
    private Dish mDish;
    View view;
    ImageView fetchPic;
    EditText name;
    EditText price;
    EditText des;

    TextView cancel;
    TextView reset;

    public interface ClickListenerInterface{
        //在activity里面调用的时候重写这些函数
        public void doResetOrSure();
        public void doCancel();
        public void doFetchPhoto();
    }

    public DishShortClickDialog(Context context, Dish d){
        super(context);
        this.mContext = context;
        this.mDish = d;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        init();
    }

    public byte[] getSrc(){
        return BitmapUtil.drawableToByteArray(fetchPic.getDrawable());
    }

    public String getName(){
        return name.getText().toString();
    }

    public String getPrice(){
        return price.getText().toString();
    }

    public String getDes(){
        return des.getText().toString();
    }

    public View getView(){
        return view;
    }

    //初始化 自定义的 dialog 组件
    private void init() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.dish_short_click_dialog, null, false);
        setContentView(view);

        fetchPic = view.findViewById(R.id.fetchPic);
        name = view.findViewById(R.id.name);
        price = view.findViewById(R.id.price);
        des = view.findViewById(R.id.des);

        cancel = view.findViewById(R.id.cancel);
        reset = view.findViewById(R.id.resetDishName);

        if(mDish != null){
            setStringsForEditTexts();
        }
        else{
            setDefault();
        }

        cancel.setOnClickListener(new clickListener());
        reset.setOnClickListener(new clickListener());
        fetchPic.setOnClickListener(new clickListener());

        setDialogAttributes();

    }



    private void setDialogAttributes() {
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics();
        lp.height = (int)(d.heightPixels * 0.4);
        lp.width = (int)(d.widthPixels * 0.85);
        dialogWindow.setAttributes(lp);
    }

    private void setDefault() {
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_add_64);
        fetchPic.setImageBitmap(bitmap);

        //将更改text改成确定
        reset.setText("确定");
    }

    private void setStringsForEditTexts() {
        fetchPic.setImageBitmap(BitmapUtil.byteArrayToBitmap(mDish.getSrc()));
        name.setText(mDish.getName());
        price.setText(mDish.getPrice());
        des.setText(mDish.getDes());

        //reset.setText("更改");
    }

    public void setClickListener(ClickListenerInterface clickListenerInterface){
        this.mClickListenerInterface = clickListenerInterface;
    }

    private class clickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.cancel:
                    mClickListenerInterface.doCancel();
                    break;
                case R.id.resetDishName:
                    mClickListenerInterface.doResetOrSure();
                    break;
                case R.id.fetchPic:
                    mClickListenerInterface.doFetchPhoto();
                    break;

            }
        }
    }

}
