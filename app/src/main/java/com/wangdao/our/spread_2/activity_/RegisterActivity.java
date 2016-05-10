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
 * Created by Administrator on 2016/4/25 0025.
 */
public class RegisterActivity extends Activity implements View.OnClickListener{
    private EditText et_tel,et_pwd,et_vf,et_it;
    private Button bt_send,bt_okr;
    private AllUrl allurl = new AllUrl();
    private String registerResult;
    private HttpPost httpPost;
    private HttpResponse httpResponse = null;
    private List<NameValuePair> params = new ArrayList<NameValuePair>();
    private String myTel,myPwd;
    private registerHandler rHandle = new registerHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        bt_okr.setOnClickListener(this);
        bt_send.setOnClickListener(this);
    }

    private void initView(){
        et_tel = (EditText) findViewById(R.id.activity_register_et_tel);
        et_pwd = (EditText) findViewById(R.id.activity_register_et_pwd);
        et_vf = (EditText) findViewById(R.id.activity_register_et_vf);
        et_it = (EditText) findViewById(R.id.activity_register_et_it);
        bt_send = (Button) findViewById(R.id.activity_register_et_send);
        bt_okr = (Button) findViewById(R.id.activity_register_bt_okr);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_register_et_send:
                sendNote();
                break;
            case R.id.activity_register_bt_okr:
                register();
                break;
        }
    }

    /**
     *  发送验证码
     */
    private String sendResult = "网络异常";
    private void sendNote(){
        httpPost = new HttpPost(allurl.getRegister_note());
        params.add(new BasicNameValuePair("mobile", et_tel.getText().toString()));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    httpResponse = new DefaultHttpClient().execute(httpPost);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        String result = EntityUtils.toString(httpResponse.getEntity());
                        JSONObject jo = new JSONObject(result);
                        sendResult = jo.getString("info");
                        if(jo.getString("status").equals("1")){
                            rHandle.sendEmptyMessage(11);
                        }else{
                            rHandle.sendEmptyMessage(12);
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


    private void register(){
        if(et_tel.getText().toString().length() !=0 || et_pwd.getText().toString().length() != 0){
            myTel = et_tel.getText().toString();
            myPwd = et_pwd.getText().toString();
            httpPost = new HttpPost(allurl.getRegister_url());

            params.add(new BasicNameValuePair("reg_verify", et_vf.getText().toString()));//手机验证
         //   params.add(new BasicNameValuePair("img_verify", "1234"));//图形验证码
          //  params.add(new BasicNameValuePair("invite_code", "1234"));//邀请码

            params.add(new BasicNameValuePair("mobile", myTel));
            params.add(new BasicNameValuePair("password", myPwd));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                        httpResponse = new DefaultHttpClient().execute(httpPost);
                        if (httpResponse.getStatusLine().getStatusCode() == 200) {
                            String result = EntityUtils.toString(httpResponse.getEntity());
                            JSONObject jo = new JSONObject(result);
                            registerResult = jo.getString("info");
                            if(jo.getString("status").equals("1")){
                                rHandle.sendEmptyMessage(1);
                            }else{
                                rHandle.sendEmptyMessage(2);
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

    class registerHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                new AlertDialog.Builder(RegisterActivity.this)
                        .setTitle("注册结果")
                        .setMessage(registerResult)
                        .setPositiveButton("去登陆", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent register  = new Intent();
                                register.putExtra("tel", myTel);
                                register.putExtra("pwd", myPwd);
                                setResult(1, register);
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
                case 2:
                    new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle("注册结果")
                            .setMessage(registerResult)
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    break;
                //发送成功
                case 11:
                    Toast.makeText(RegisterActivity.this,sendResult,Toast.LENGTH_SHORT).show();
                    break;
                //发送失败
                case 12:
                    Toast.makeText(RegisterActivity.this,sendResult,Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    }
}
