package com.wangdao.our.spread_2.activity_;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wangdao.our.spread_2.R;
import com.wangdao.our.spread_2.activity_.mine_activity.BindingAccount;
import com.wangdao.our.spread_2.activity_.mine_activity.GetMoney;
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
 * Created by Administrator on 2016/5/10 0010.
 */
public class TiXianToSomeHere extends Activity implements View.OnClickListener{

    private HttpPost httpPost;
    private HttpResponse httpResponse = null;
    private List<NameValuePair> params = new ArrayList<NameValuePair>();
    private AllUrl allurl = new AllUrl();


    private EditText et_money;
    private TextView tv_userAllMoney;
    private Button bt_tixian;
    private TextView tv_actionbar;
    private int tixianNum = 1;
    private TiXianHandler txHandler = new TiXianHandler();
    private ImageView iv_cancle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tixian);
        initView();
        initClick();
        Intent getData = getIntent();
        String userBuyType = getData.getExtras().getString("buy_type");
        if(userBuyType.equals("微信")){
            tixianNum =1;
        }else{
            tixianNum = 2;
        }
        tv_actionbar.setText("提现到:"+userBuyType);
        initUserMoney();
    }

    private void initView(){
        tv_userAllMoney = (TextView) findViewById(R.id.activity_tixian_tv_allmoney);
        bt_tixian = (Button) findViewById(R.id.activity_tixian_bt_tixian);
        tv_actionbar = (TextView) findViewById(R.id.activity_tixian_tv_actiobar);
        et_money = (EditText) findViewById(R.id.activity_tixian_et_money);
        iv_cancle= (ImageView) findViewById(R.id.activity_tixian_iv_cancle);
    }

    private void initClick(){
        bt_tixian.setOnClickListener(this);
        iv_cancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_tixian_bt_tixian:
                tixian();
                break;
            case R.id.activity_tixian_iv_cancle:
                finish();
                break;
        }
    }

    /**
     * 提现
     */
    private String tixianResult = "网络异常";
    private void tixian(){

        httpPost = new HttpPost(allurl.getTixian());
        SharedPreferences sharedPreferences = TiXianToSomeHere.this.getSharedPreferences("user", MODE_PRIVATE);
        String mToken = sharedPreferences.getString("user_token", "");

        params.add(new BasicNameValuePair("user_token", mToken));
        params.add(new BasicNameValuePair("price", et_money.getText().toString()));
        if(tixianNum ==1){
            params.add(new BasicNameValuePair("pay_way", "wx_pub"));
        }else{
            params.add(new BasicNameValuePair("pay_way", "alipay_wap"));
        }
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    httpResponse = new DefaultHttpClient().execute(httpPost);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        String result = EntityUtils.toString(httpResponse.getEntity());
                        JSONObject jo = new JSONObject(result);
                        tixianResult = jo.getString("info");
                        if(jo.getString("status").equals("1")){
                            txHandler.sendEmptyMessage(1);
                        }else{
                            txHandler.sendEmptyMessage(2);
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

    /**
     * 初始化余额
     */
    private String getResult = "0.00";
    private void initUserMoney(){

        httpPost = new HttpPost(allurl.getTixianIfo());
        SharedPreferences sharedPreferences = TiXianToSomeHere.this.getSharedPreferences("user", MODE_PRIVATE);
        String mToken = sharedPreferences.getString("user_token", "");
        params.add(new BasicNameValuePair("user_token", mToken));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    httpResponse = new DefaultHttpClient().execute(httpPost);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        String result = EntityUtils.toString(httpResponse.getEntity());
                        JSONObject jo = new JSONObject(result);
                        if(jo.getString("status").equals("1")){
                            JSONObject jo_2 = jo.getJSONObject("data");
                            getResult = jo_2.getString("账户资金");
                            txHandler.sendEmptyMessage(11);
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
    class TiXianHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //提箱成功
                case 1:
                    new AlertDialog.Builder(TiXianToSomeHere.this)
                            .setTitle("RESULT：")
                            .setMessage(tixianResult)
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    initUserMoney();
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    break;
                //提现失败
                case 2:

                    new AlertDialog.Builder(TiXianToSomeHere.this)
                            .setTitle("RESULT：")
                            .setMessage(tixianResult)
                            .setPositiveButton("去绑定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent bIntent  = new Intent(TiXianToSomeHere.this, BindingAccount.class);
                                    startActivity(bIntent);
                                    finish();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    break;
                //更新余额
                case 11:
                    tv_userAllMoney.setText(getResult);
                    break;
            }
        }
    }


}

