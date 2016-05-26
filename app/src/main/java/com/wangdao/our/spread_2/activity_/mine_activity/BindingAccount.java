package com.wangdao.our.spread_2.activity_.mine_activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
 * Created by Administrator on 2016/5/10 0010.
 * 绑定账户
 */
public class BindingAccount extends Activity implements View.OnClickListener{

    private ImageView iv_cancle;
    private Button bt_bingd;
    private EditText et_wx,et_aripay,et_name;

    private HttpPost httpPost;
    private HttpPost httpPost_2;
    private HttpResponse httpResponse = null;
    private List<NameValuePair> params = new ArrayList<NameValuePair>();
    private AllUrl allurl = new AllUrl();
    private BindingHandler bdHandler = new BindingHandler();
    private String TName,TWx,TAripay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_bd);
        initView();
        initClick();
        initAccount();
    }

    private void initView(){
        iv_cancle = (ImageView) findViewById(R.id.activity_mine_bd_iv_cancle);
        bt_bingd = (Button) findViewById(R.id.activity_mine_bd_bt_ok);
        et_wx = (EditText) findViewById(R.id.activity_mine_bd_et_wx);
        et_aripay = (EditText) findViewById(R.id.activity_mine_bd_et_airpay);
        et_name = (EditText) findViewById(R.id.activity_mine_bd_et_name);
    }
    private void initClick(){
        iv_cancle.setOnClickListener(this);
        bt_bingd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_mine_bd_iv_cancle:
                finish();
                break;
            case R.id.activity_mine_bd_bt_ok:
                startBingd();
                break;
        }
    }
    private String bindingResult = "网络异常";
    private void startBingd(){
        TName = et_name.getText().toString();
        TWx = et_wx.getText().toString();
        TAripay = et_aripay.getText().toString();

        Log.i("qqqqq","TAripayTAripayTAripayTAripay"+TAripay);


        if(TName.length() == 0){
            Toast.makeText(BindingAccount.this,"请填写真实姓名",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TAripay.length() == 0 && TWx.length() == 0 ){
            Toast.makeText(BindingAccount.this,"请填写账号",Toast.LENGTH_SHORT).show();
            return;
        }

        httpPost = new HttpPost(allurl.getChange_user_info());
        SharedPreferences sharedPreferences = BindingAccount.this.getSharedPreferences("user", MODE_PRIVATE);
        params.add(new BasicNameValuePair("user_token", sharedPreferences.getString("user_token", "")));
        params.add(new BasicNameValuePair("truename", TName));

        if(TWx.length() != 0){
            params.add(new BasicNameValuePair("wx_num",TWx));
        }
        if(TAripay.length() != 0){
            params.add(new BasicNameValuePair("alipay_num",TAripay));
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
                        bindingResult = jo.getString("info");
                        if(jo.getString("status").equals("1")){
                            bdHandler.sendEmptyMessage(1);
                        }else{
                            bdHandler.sendEmptyMessage(2);
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

    class BindingHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //绑定成功
                case 1:
                    new AlertDialog.Builder(BindingAccount.this)
                            .setTitle("提醒")
                            .setMessage(bindingResult)
                            .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    setResult(5);
                                    finish();
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    break;
                //绑定失败
                case 2:
                    new AlertDialog.Builder(BindingAccount.this)
                            .setTitle("提醒")
                            .setMessage(bindingResult)
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    break;
                case 11:
                    et_name.setText(TName);
                    et_aripay.setText(TAripay);
                    et_wx.setText(TWx);
                    break;
            }
        }
    }

    /**
     * 初始化账户数据
     */

    private void initAccount(){

        httpPost_2 = new HttpPost(allurl.getUserAllInfo_all());
        SharedPreferences sharedPreferences = BindingAccount.this.getSharedPreferences("user", MODE_PRIVATE);
        String uToken = sharedPreferences.getString("user_token", "");
        params.add(new BasicNameValuePair("user_token", uToken));

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    httpPost_2.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    httpResponse = new DefaultHttpClient().execute(httpPost_2);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        String result = EntityUtils.toString(httpResponse.getEntity());
                        JSONObject jo = new JSONObject(result);
                        if(jo.getString("status").equals("1")){

                            JSONObject jo_2 = jo.getJSONObject("data");
                            TName = jo_2.getString("truename");
                            TAripay = jo_2.getString("alipay_num");
                            TWx = jo_2.getString("wx_num");

                            bdHandler.sendEmptyMessage(11);
                        }else{
                            bdHandler.sendEmptyMessage(12);
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
}
