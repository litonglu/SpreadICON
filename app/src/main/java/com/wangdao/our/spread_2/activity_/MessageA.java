package com.wangdao.our.spread_2.activity_;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wangdao.our.spread_2.R;
import com.wangdao.our.spread_2.slide_widget.widget_push.ExampleUtil;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/4/29 0029.
 */
public class MessageA extends Activity implements View.OnClickListener{
    //for receive customer msg from jpush server
    //private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public static boolean isForeground = false;
    private MessageReceiver mMessageReceiver;
    private ImageView iv_cancle;
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_message);
        iv_cancle = (ImageView) findViewById(R.id.messagea_iv_cancle);
        registerMessageReceiver();  // used for receive msg
        iv_cancle.setOnClickListener(this);
    }


    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.messagea_iv_cancle:
                finish();
                break;
        }
    }


    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!ExampleUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
                setCostomMsg(showMsg.toString());
            }
        }
    }

    private void setCostomMsg(String msg){
        if (null != msg) {
            Log.i("qqqqq","接受的消息-----"+msg);
            tv.setText(msg);
        }
        Log.i("qqqqq","接受的消息-----kong");
        tv.setText("kong");
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
    // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
    private void init(){
        JPushInterface.init(getApplicationContext());
    }

}
