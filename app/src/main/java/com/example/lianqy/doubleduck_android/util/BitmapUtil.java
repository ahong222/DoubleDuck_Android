package com.example.lianqy.doubleduck_android.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import com.example.lianqy.doubleduck_android.R;
import com.example.lianqy.doubleduck_android.ui.ManageDishes.ManageDishesActivity;

import java.io.ByteArrayOutputStream;

public class BitmapUtil {
    public static byte[] drawableToByteArray(Drawable drawable){
        if (drawable != null) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            int size = bitmap.getWidth() * bitmap.getHeight() * 4;
            // 创建一个字节数组输出流,流的大小为size
            ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
            // 设置位图的压缩格式，质量为100%，并放入字节数组输出流中
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            // 将字节数组输出流转化为字节数组byte[]
            byte[] imagedata = baos.toByteArray();
            return imagedata;
        }
        return null;
    }

    public static byte[] bitmapToByteArray(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] array = baos.toByteArray();
        return array;
    }

    public  static Bitmap byteArrayToBitmap(byte[] array){
        if (array.length != 0) {
            return BitmapFactory.decodeByteArray(array, 0, array.length);
        }
        else {
            return null;
        }

    }

    public static byte[] getDefaultByteArray(Context context) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_add_64);
        return bitmapToByteArray(bitmap);
    }

    public static byte[] getDefaultLogoByteArray(Context context) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo1);
        return bitmapToByteArray(bitmap);
    }
}
