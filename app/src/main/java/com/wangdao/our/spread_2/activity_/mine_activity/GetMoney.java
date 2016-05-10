package com.wangdao.our.spread_2.activity_.mine_activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.wangdao.our.spread_2.R;

/**
 * Created by Administrator on 2016/5/9 0009.
 * 提现
 */
public class GetMoney extends Activity implements View.OnClickListener{
   private ImageView iv_cancle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getmoney);
        initView();
        initClick();
    }
    private void initView(){
        iv_cancle = (ImageView) findViewById(R.id.activity_mine_getmonet_iv_cancle);
    }
    private void initClick(){
        iv_cancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_mine_getmonet_iv_cancle:
                finish();
                break;
        }
    }
}
