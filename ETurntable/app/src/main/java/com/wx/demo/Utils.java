package com.wx.demo;

import android.content.Context;
import android.view.WindowManager;

/**
 * Created by wangxuan on 2017/10/10.
 */

public class Utils {

    public static int getWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return manager.getDefaultDisplay().getWidth();
    }

    public static int getHeigth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return manager.getDefaultDisplay().getHeight();
    }

    public static int getMinLenght(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return Math.min(getHeigth(context),getWidth(context));
    }

    public static int getMaxLenght(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return Math.max(getHeigth(context),getWidth(context));
    }

}
