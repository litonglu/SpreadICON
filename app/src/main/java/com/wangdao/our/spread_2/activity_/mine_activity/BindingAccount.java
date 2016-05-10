package com.wangdao.our.spread_2.activity_.mine_activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.wangdao.our.spread_2.R;
import com.wangdao.our.spread_2.activity_.ChangeNickName;
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
    private HttpResponse httpResponse = null;
    private List<NameValuePair> params = new ArrayList<NameValuePair>();
    private AllUrl allurl = new AllUrl();
    private BindingHandler bdHandler = new BindingHandler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_bd);
        initView();
        initClick();
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
        if(et_name.getText().toString().length() == 0){
            return;
        }
        if(et_aripay.getText().toString().length() == 0 && et_wx.getText().toString().length() == 0 ){
            return;
        }
        if(et_name.getText().toString().length() ==0){
            return;
        }
        httpPost = new HttpPost(allurl.getChange_user_info());
        SharedPreferences sharedPreferences = BindingAccount.this.getSharedPreferences("user", MODE_PRIVATE);
        params.add(new BasicNameValuePair("user_token", sharedPreferences.getString("user_token", "")));
        params.add(new BasicNameValuePair("truename", et_name.getText().toString()));

        if(et_wx.getText().toString().length() != 0){
            params.add(new BasicNameValuePair("wx_num",et_wx.getText().toString()));
        }
        if(et_aripay.getText().toString().length() != 0){
            params.add(new BasicNameValuePair("alipay_num ",et_aripay.getText().toString()));
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
            }
        }
    }
}
