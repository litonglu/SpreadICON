package com.wangdao.our.spread_2.activity_.mine_activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.wangdao.our.spread_2.R;
import com.wangdao.our.spread_2.slide_widget.AllUrl;

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
import java.util.List;

/**
 * Created by Administrator on 2016/5/12 0012.
 */
public class CommissionInfo extends Activity implements View.OnClickListener{

    private ImageView iv_cancle;
    private TextView tv_jiaoyi,tv_money,tv_time,tv_info,tv_payway;
    private Button bt_get;
    private String uNum,uPrice,uPayWay,uInfo,uTime,uId,uStatus;

    private HttpPost httpPost;
    private HttpResponse httpResponse = null;
    private List<NameValuePair> params = new ArrayList<NameValuePair>();
    private AllUrl allurl = new AllUrl();

    private ciHandler chandler = new ciHandler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_commission);

        Intent userData = getIntent();
        uNum = userData.getExtras().getString("jiao");
        uPrice = userData.getExtras().getString("price");
        uPayWay = userData.getExtras().getString("payway");
        uInfo = userData.getExtras().getString("info");
        uTime = userData.getExtras().getString("time");
        uId = userData.getExtras().getString("id");
        uStatus = userData.getExtras().getString("status");


        initView();
        initClick();
        initData();
        if(uStatus.equals("used")){
            bt_get.setText("该佣金已领取过");
            bt_get.setClickable(false);
        }
    }

    private void initView(){
        iv_cancle = (ImageView) findViewById(R.id.activity_get_commission_iv_cancle);
        tv_jiaoyi = (TextView) findViewById(R.id.activity_get_commission_tv_jiaoyi);
        tv_money = (TextView) findViewById(R.id.activity_get_commission_tv_money);
        tv_time = (TextView) findViewById(R.id.activity_get_commission_tv_time);
        tv_info = (TextView) findViewById(R.id.activity_get_commission_tv_info);
        tv_payway = (TextView) findViewById(R.id.activity_get_commission_tv_payway);
        bt_get = (Button) findViewById(R.id.activity_get_commission_bt_get);
    }
    private void initClick(){
        iv_cancle.setOnClickListener(this);
        bt_get.setOnClickListener(this);
        bt_get.setBackgroundColor(getResources().getColor(R.color.textcolor_hui));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_get_commission_iv_cancle :
                    finish();
                break;
            case R.id.activity_get_commission_bt_get :
                staraGetMoney();
                break;
        }
    }

    /**
     * 初始化数据
     */
    private void initData(){
        tv_jiaoyi.setText(uNum);
        tv_money.setText(uPrice);
        tv_time.setText(uTime);
        tv_info.setText(uInfo);
        tv_payway.setText(uPayWay);
    }

    /**
     * 立刻领取佣金
     */
    private String getResult = "网络异常";
    private void staraGetMoney(){
        httpPost = new HttpPost(allurl.getGeCommission());

        SharedPreferences sharedPreferences = CommissionInfo.this.getSharedPreferences("user", MODE_PRIVATE);
        String mToken = sharedPreferences.getString("user_token", "");

        params.add(new BasicNameValuePair("user_token", mToken));
        params.add(new BasicNameValuePair("brokerage_id", uId));

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    httpResponse = new DefaultHttpClient().execute(httpPost);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        String result = EntityUtils.toString(httpResponse.getEntity());
                        JSONObject jo = new JSONObject(result);
                        getResult = jo.getString("info");
                        if(jo.getString("status").equals("1")){
                            chandler.sendEmptyMessage(1);
                        }else{
                            chandler.sendEmptyMessage(2);
                        }

                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
         }).start();
    }

    class ciHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //领取成功
                case 1:
                    new AlertDialog.Builder(CommissionInfo.this)
                            .setTitle("RESULT:")
                            .setMessage(getResult)
                            .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    setResult(2);
                                    finish();
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    break;
                case 2:
                    new AlertDialog.Builder(CommissionInfo.this)
                            .setTitle("RESULT:")
                            .setMessage(getResult)
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    break;
            }
        }
    }
}
