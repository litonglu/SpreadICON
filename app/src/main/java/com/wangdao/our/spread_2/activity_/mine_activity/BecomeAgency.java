package com.wangdao.our.spread_2.activity_.mine_activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
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

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/5/9 0009.
 * 成为代理
 */
public class BecomeAgency extends Activity implements View.OnClickListener{
    private ImageView iv_cancle;
    private TextView tv_buy;
    private static String YOUR_URL ="http://wz.ijiaque.com/app/toup/vip_toup.html";
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

    private boolean bRb_1 = true;
    private boolean bRb_2 = false;
    private String Str_type ;
    private HttpPost httpPost;
    private HttpResponse httpResponse = null;
    private List<NameValuePair> params = new ArrayList<NameValuePair>();
    private Button bt_buy;
    private RadioButton rb_wx,rb_zfb;
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
        rb_wx = (RadioButton) findViewById(R.id.activity_mine_agency_rb_wx);
        rb_zfb = (RadioButton) findViewById(R.id.activity_mine_agency_rb_zfb);
        bt_buy = (Button) findViewById(R.id.activity_mine_agency_bt);
    }
    private void initClick(){
        iv_cancle.setOnClickListener(this);
        tv_buy.setOnClickListener(this);
        bt_buy.setOnClickListener(this);
        rb_wx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(bRb_1){
                    rb_wx.setChecked(false);
                    rb_zfb.setChecked(true);
                    bRb_1 = false;
                    bRb_2 = true;
                }else{
                    rb_wx.setChecked(true);
                    rb_zfb.setChecked(false);
                    bRb_1 = true;
                    bRb_2 = false;
                }
            }
        });
        rb_zfb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(bRb_2){
                    rb_zfb.setChecked(false);
                    rb_wx.setChecked(true);
                    bRb_2 = false;
                    bRb_1 = true;
                }else{
                    rb_zfb.setChecked(true);
                    rb_wx.setChecked(false);
                    bRb_2 = true;
                    bRb_1 = false;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_mine_agency_iv__cancle:
                setResult(2);
                finish();
                break;
            case R.id.activity_mine_agency_tv_buy:
                showBuyTypeDialog();
                break;
            case R.id.activity_mine_agency_bt:

                if(bRb_1){
                    startBuy("wx");
                    Str_type = "wx";
                }else{
                    startBuy("alipay");
                    Str_type = "alipay";
                }
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
                Str_type = "wx";
                dialog_help_2.dismiss();
            }
        });
        hi_2.tv_help2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBuy("alipay");
                Str_type = "alipay";
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
        Log.i("qqqqq","type=="+ buyType );
    }

    private String payResult = "网络异常";
    class PaymentTask extends AsyncTask<PaymentRequest, Void, String> {
        private JSONObject jo_2;
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
//            String data = null;
//            String json = new Gson().toJson(paymentRequest);
//            try {
//                //向Your Ping++ Server SDK请求数据
//                data = postJson(URL, json);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            httpPost = new HttpPost(URL);
            SharedPreferences sharedPreferences = BecomeAgency.this.getSharedPreferences("user", MODE_PRIVATE);
            String mToken = sharedPreferences.getString("user_token", "");
            params.add(new BasicNameValuePair("user_token", mToken));
            params.add(new BasicNameValuePair("channel", Str_type));
            params.add(new BasicNameValuePair("rank_id", "10"));

            Log.i("qqqqqq","channel=="+Str_type);
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                httpResponse = new DefaultHttpClient().execute(httpPost);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    String result = EntityUtils.toString(httpResponse.getEntity());
                    JSONObject jo = new JSONObject(result);
                    payResult = jo.getString("info");
                    if(jo.getString("status").equals("1")) {
                        jo_2 = jo.getJSONObject("data");
                    }else{
                        return null;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jo_2.toString();
        }

        /**
         * 获得服务端的charge，调用ping++ sdk。
         */
        @Override
        protected void onPostExecute(String data) {
            if(null == data){
                showMsg("", payResult, "");
                return;
            }
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
        builder.setTitle("RESULT:");
        builder.setPositiveButton("取消", null);
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
                if(result.equals("success")){
                    SharedPreferences sharedPreferences = BecomeAgency.this.getSharedPreferences("user", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("isvip", "1");
                    editor.commit();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        setResult(2);
        finish();
    }
}
