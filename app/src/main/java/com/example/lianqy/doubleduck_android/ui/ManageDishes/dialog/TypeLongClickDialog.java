package com.example.lianqy.doubleduck_android.ui.ManageDishes.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lianqy.doubleduck_android.R;
import com.example.lianqy.doubleduck_android.model.Type;

public class TypeLongClickDialog extends Dialog {
    private Context mContext;
    private TypeLongClickDialog.ClickListenerInterface mClickListenerInterface;

    private Type mType;
    View view;
    EditText typeName;
    TextView reset;
    TextView cancel;
    TextView delete;

    public interface ClickListenerInterface{
        public void doReset();
        public void doCancel();
        public void doDeleteOrSure();
    }

    public TypeLongClickDialog(Context context, Type t){
        super(context);
        this.mContext = context;
        this.mType = t;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.type_long_click_dialog, null, false);
        setContentView(view);

        typeName = view.findViewById(R.id.editTypeName);
        reset = view.findViewById(R.id.resetTypeName);

        cancel = view.findViewById(R.id.cancelTypeDialog);
        delete = view.findViewById(R.id.deleteTypeDialog);

        if(mType != null){
            setTypeName();
        }
        else{
            changeTexts();
        }

        reset.setOnClickListener(new clickListener());
        cancel.setOnClickListener(new clickListener());
        delete.setOnClickListener(new clickListener());

        setDialogAttributes();

    }

    private void changeTexts() {
        typeName.setText("");
        delete.setText("确定");

        //隐藏而更改 text
        reset.setText("");
    }


    private void setTypeName() {
        typeName.setText(mType.getType());
    }

    private void setDialogAttributes() {
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics();
        lp.height = (int)(d.heightPixels * 0.3);
        lp.width = (int)(d.widthPixels * 0.85);
        dialogWindow.setAttributes(lp);
    }

    public String getTypeName(){
        return typeName.getText().toString();
    }

    public void setClickListener(ClickListenerInterface clickListenerInterface){
        this.mClickListenerInterface = clickListenerInterface;
    }

    private class clickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id){
                case R.id.resetTypeName:
                    mClickListenerInterface.doReset();
                    break;
                case R.id.cancelTypeDialog:
                    mClickListenerInterface.doCancel();
                    break;
                case R.id.deleteTypeDialog:
                    mClickListenerInterface.doDeleteOrSure();
                    break;
            }
        }
    }


}
