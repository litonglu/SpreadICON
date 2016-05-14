package com.wangdao.our.spread_2.activity_;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.wangdao.our.spread_2.R;

/**
 * Created by Administrator on 2016/5/13 0013.
 * 为防止Fragment错误的缓冲Activity
 */
public class myZidingyi extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zidingyi);
        Log.i("qqqqq","过来了");
        finish();
    }
}
