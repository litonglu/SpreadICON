package com.wangdao.our.spread_2.slide_widget.widget_image;

public class Log {

    private static final String TAG = "android-crop";

    public static final void e(String msg) {
        android.util.Log.e(TAG, msg);
    }

    public static final void e(String msg, Throwable e) {
        android.util.Log.e(TAG, msg, e);
    }

}
