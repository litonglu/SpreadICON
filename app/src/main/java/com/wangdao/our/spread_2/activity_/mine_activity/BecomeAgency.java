package com.wangdao.our.spread_2.activity_.mine_activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pingplusplus.android.Pingpp;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.wangdao.our.spread_2.R;
import com.wangdao.our.spread_2.demo;

import java.io.IOException;
import java.util.Date;

/**
 * Created by Administrator on 2016/5/9 0009.
 * 成为代理
 */
public class BecomeAgency extends Activity implements View.OnClickListener{
    private ImageView iv_cancle;
    private TextView tv_buy;
    private static String YOUR_URL ="http://218.244.151.190/demo/charge";
    public static final String URL = YOUR_URL;
    /**
     * 银联支付渠道
     */
    private static final String CHANNEL_UPACP = "upacp";
    /**
     * 微信支付渠道
     */
    private static final String CHANNEL_WECHAT = "wx";
    /**
     * 支付支付渠道
     */
    private static final String CHANNEL_ALIPAY = "alipay";
    /**
     * 百度支付渠道
     */
    private static final String CHANNEL_BFB = "bfb";
    /**
     * 京东支付渠道
     */
    private static final String CHANNEL_JDPAY_WAP = "jdpay_wap";
    private EditText amountEditText;
    private Button wechatButton;
    private Button alipayButton;
//    private Button upmpButton;
//    private Button bfbButton;
//    private Button jdpayButton;
    private String currentAmount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_agency);
        initView();
        initClick();
    }
    private void initView(){
        iv_cancle = (ImageView) findViewById(R.id.activity_mine_agency_iv__cancle);
        tv_buy = (TextView) findViewById(R.id.activity_mine_agency_tv_buy);
    }
    private void initClick(){
        iv_cancle.setOnClickListener(this);
        tv_buy.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_mine_agency_iv__cancle:
                finish();
                break;
            case R.id.activity_mine_agency_tv_buy:
                showBuyTypeDialog();
                break;
        }
    }


    private Dialog dialog_help_2;
    View view_2;
    helpdialog_item_2 hi_2 = null;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showBuyTypeDialog() {

            hi_2 = new helpdialog_item_2();
            view_2 = getLayoutInflater().inflate(R.layout.dialog_out_login, null);
            hi_2.tv_help1 = (TextView) view_2.findViewById(R.id.bt_help1);
            hi_2.tv_help2 = (TextView) view_2.findViewById(R.id.bt_help2);

            hi_2.tv_helpcancle = (TextView) view_2.findViewById(R.id.bt_helpcancle);

            dialog_help_2 = new Dialog(this, R.style.transparentFrameWindowStyle);
            dialog_help_2.setContentView(view_2, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            Window window = dialog_help_2.getWindow();
            window.setWindowAnimations(R.style.main_menu_animstyle);
            WindowManager.LayoutParams wl = window.getAttributes();
            wl.x = 0;
            wl.y = this.getWindowManager().getDefaultDisplay().getHeight();
            wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
            wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog_help_2.onWindowAttributesChanged(wl);
            dialog_help_2.setCanceledOnTouchOutside(true);


        hi_2.tv_help1.setText("微信支付");
        hi_2.tv_help2.setText("支付宝支付");

        hi_2.tv_help1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBuy("wx");
                dialog_help_2.dismiss();
            }
        });
        hi_2.tv_help2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBuy("alipay");
                dialog_help_2.dismiss();
            }
        });
        hi_2.tv_helpcancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_help_2.dismiss();
            }
        });
        dialog_help_2.show();
    }

    class helpdialog_item_2 {
        TextView tv_help1;
        TextView tv_help2;
        TextView tv_helpcancle;
    }

    /**
     * 开始购买，调起支付
     */
    private void startBuy(String buyType){
        new PaymentTask().execute(new PaymentRequest(buyType, 298));
    }


    class PaymentTask extends AsyncTask<PaymentRequest, Void, String> {

        @Override
        protected void onPreExecute() {
            //按键点击之后的禁用，防止重复点击
//            wechatButton.setOnClickListener(null);
//            alipayButton.setOnClickListener(null);
//            upmpButton.setOnClickListener(null);
//            bfbButton.setOnClickListener(null);
        }

        @Override
        protected String doInBackground(PaymentRequest... pr) {

            PaymentRequest paymentRequest = pr[0];
            String data = null;
            String json = new Gson().toJson(paymentRequest);
            try {
                //向Your Ping++ Server SDK请求数据
                data = postJson(URL, json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }


        /**
         * 获得服务端的charge，调用ping++ sdk。
         */
        @Override
        protected void onPostExecute(String data) {
            if(null == data){
                showMsg("请求出错", "请检查URL", "URL无法获取charge");
                return;
            }
            Log.d("charge", data);
            Pingpp.createPayment(BecomeAgency.this, data);
        }
    }
    public void showMsg(String title, String msg1, String msg2) {
        String str = title;
        if (null !=msg1 && msg1.length() != 0) {
            str += "\n" + msg1;
        }
        if (null !=msg2 && msg2.length() != 0) {
            str += "\n" + msg2;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(BecomeAgency.this);
        builder.setMessage(str);
        builder.setTitle("提示");
        builder.setPositiveButton("OK", null);
        builder.create().show();
    }

    private static String postJson(String url, String json) throws IOException {
        MediaType type = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(type, json);
        Request request = new Request.Builder().url(url).post(body).build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    class PaymentRequest {
        String channel;
        int amount;

        public PaymentRequest(String channel, int amount) {
            this.channel = channel;
            this.amount = amount;
        }
    }
    /**
     * onActivityResult 获得支付结果，如果支付成功，服务器会收到ping++ 服务器发送的异步通知。
     * 最终支付成功根据异步通知为准
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

//        wechatButton.setOnClickListener(BecomeAgency.this);
//        alipayButton.setOnClickListener(BecomeAgency.this);
//        upmpButton.setOnClickListener(BecomeAgency.this);
//        bfbButton.setOnClickListener(BecomeAgency.this);

        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                showMsg(result, errorMsg, extraMsg);
            }
        }
    }
}
